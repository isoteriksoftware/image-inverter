package actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.encentral.scaffold.commons.utils.ImageInverter;

/**
 * An actor responsible for handling image file inversion concurrently.
 */
public class ImageInverterActor extends AbstractActor {
    public static Props getProps() {
        return Props.create(ImageInverterActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ImageInverterActorProtocol.ImageInversionRequest.class, imageInversionRequest -> {
                    boolean success = ImageInverter.invertImage(imageInversionRequest.source,
                            imageInversionRequest.destination);
                    sender().tell(new ImageInverterActorProtocol.ImageInversionResponse(success,
                            imageInversionRequest.source, imageInversionRequest.destination), self());
                })
                .build();
    }
}
