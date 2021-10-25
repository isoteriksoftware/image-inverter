package controllers;

import actors.ImageInverterActor;
import actors.ImageInverterActorProtocol;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import static akka.pattern.Patterns.ask;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import io.swagger.util.Json;
import play.api.Play;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import scala.compat.java8.FutureConverters;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
@Api(value = "Image Inverter")
public class ImageInverterController extends Controller {
    final ActorRef imageInverterActor;

    final String UPLOAD_PATH = "public/uploads/";

    final ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    public ImageInverterController(ActorSystem actorSystem) {
        imageInverterActor = actorSystem.actorOf(ImageInverterActor.getProps());
    }

    @ApiOperation(value = "Upload and invert an image")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = SuccessResponse.class, message = "Image inverted successfully"),
            @ApiResponse(code = 400, message = "No image file provided"),
            @ApiResponse(code = 400, message = "Provided file is not a valid image file"),
            @ApiResponse(code = 500, message = "Failed to invert image file")
    })
    @ApiImplicitParams(value = {
            @ApiImplicitParam(
                    name = "image",
                    paramType = "form",
                    required = true,
                    type = "file",
                    value = "The image file to invert"
            )
    })
    public CompletionStage<Result> invertImage() {
        Http.MultipartFormData<File> body = request().body().asMultipartFormData();
        if (body == null)
            return CompletableFuture.supplyAsync(() -> badRequest("No image file provided"));

        Http.MultipartFormData.FilePart<File> image = body.getFile("image");

        if (image == null)
            return CompletableFuture.supplyAsync(() -> badRequest("No image file provided"));
        else {
            String fileName = image.getFilename();
            String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (!extension.equals("png") && !extension.equals("jpg") && !extension.equals("jpeg"))
                return CompletableFuture.supplyAsync(() -> badRequest("Provided file is not a valid image file"));

            File source = image.getFile();
            File destination = Play.current().getFile(UPLOAD_PATH + UUID.randomUUID() + ".png");
            ImageInverterActorProtocol.ImageInversionRequest inversionRequest =
                    new ImageInverterActorProtocol.ImageInversionRequest(source, destination);

            return FutureConverters.toJava(ask(imageInverterActor, inversionRequest, 5000))
                    .thenApply(response -> {
                        ImageInverterActorProtocol.ImageInversionResponse res =
                                (ImageInverterActorProtocol.ImageInversionResponse) response;

                        if (res.successful) {
                            SuccessResponse successResponse = new SuccessResponse();
                            successResponse.message = "Image inverted successfully";
                            successResponse.imageFileName = destination.getPath().substring(
                                    destination.getPath().lastIndexOf("/") + 1);
                            successResponse.usageInstructions = "Use the provided imageFileName to send a request to the /image " +
                                    "GET endpoint";

                            try {
                                return ok(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(successResponse));
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                                return internalServerError();
                            }
                        }
                        else
                            return internalServerError("Failed to invert image file");
                    });
        }
    }

    @ApiOperation(value = "Get an inverted image")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = File.class, message = "Found image"),
            @ApiResponse(code = 404, message = "Image not found")
    })
    public Result getImage(String imageFileName) {
        File file = Play.current().getFile(UPLOAD_PATH + imageFileName);

        if (file.exists()) {
            return ok(file);
        }
        else
            return notFound("Image not found");
    }

    @ApiOperation(value = "Get all inverted images")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = List.class, message = "Found image"),
            @ApiResponse(code = 404, message = "No inverted image files yet")
    })
    public Result getAllImages() throws JsonProcessingException {
        File[] files = Play.current().getFile(UPLOAD_PATH).listFiles();

        if (files == null || files.length == 0)
            return notFound("No inverted image files yet");
        else {
            List<String> list = new ArrayList<>();
            for (File file : files)
                list.add(file.getPath().substring(file.getPath().lastIndexOf("/") + 1));

            return ok(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(list));
        }
    }

    private static class SuccessResponse {
        public String message;
        public String imageFileName;
        public String usageInstructions;
    }
}


















