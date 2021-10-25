package actors;

import java.io.File;

public class ImageInverterActorProtocol {
    public static class ImageInversionRequest {
        public final File source, destination;

        public ImageInversionRequest(File source, File destination) {
            this.source = source;
            this.destination = destination;
        }
    }

    public static class ImageInversionResponse {
        public final boolean successful;
        public final File source, destination;

        public ImageInversionResponse(boolean successful, File source, File destination) {
            this.successful = successful;
            this.source = source;
            this.destination = destination;
        }
    }
}
