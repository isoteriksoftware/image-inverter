package controllers;

import actors.ImageInverterActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import io.swagger.annotations.*;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
@Api(value = "Image Inverter")
public class ImageInverterController extends Controller {
    final ActorRef imageInverterActor;

    final String UPLOAD_PATH = "public/uploads/";

    @Inject
    public ImageInverterController(ActorSystem actorSystem) {
        imageInverterActor = actorSystem.actorOf(ImageInverterActor.getProps());
    }

    @ApiOperation(value = "Upload image to invert")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Image inverted")
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
            File file = image.getFile();
            return CompletableFuture.supplyAsync(() ->
                    ok(file.getAbsolutePath()));
        }
    }
}


















