package actors;

import java.io.File;

/**
 * This class holds inner classes that represents messages that can be sent and received by {@link ImageInverterActor}.
 */
public class ImageInverterActorProtocol {
    /**
     * An instance of this class represents a message requesting for the inversion of an image file.
     */
    public static class ImageInversionRequest {
        public final File source, destination;

        public ImageInversionRequest(File source, File destination) {
            this.source = source;
            this.destination = destination;
        }
    }

    /**
     * An instance of this class represents a message signifying the response of an inversion request.
     * {@link #successful} is set to true if the image was successfully inverted without any errors
     */
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
