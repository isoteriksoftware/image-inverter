package controllers;

import actors.ImageInverterActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
public class ImageInverterController extends Controller {
    final ActorRef imageInverterActor;

    final String UPLOAD_PATH = "public/uploads/";

    @Inject
    public ImageInverterController(ActorSystem actorSystem) {
        imageInverterActor = actorSystem.actorOf(ImageInverterActor.getProps());
    }

    public CompletionStage<Result> invertImage() {
        Http.MultipartFormData<File> body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart<File> image = body.getFile("image");

        if (image == null)
            return new CompletableFuture<>().thenApply(response ->
                    badRequest("No image file provided"));
        else {
            File file = image.getFile();
            return new CompletableFuture<>().thenApply(response ->
                    ok(file.getAbsolutePath()));
        }
    }
}


















