package com.encentral.image_inverter.actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.encentral.image_inverter.api.ImageInverter;

/**
 * An actor responsible for handling image file inversion concurrently.
 */
public class ImageInverterActor extends AbstractActor {
    private ImageInverter imageInverter;

    public static Props getProps() {
        return Props.create(ImageInverterActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ImageInverter.class, imageInverter -> this.imageInverter = imageInverter)
                .match(ImageInverterActorProtocol.ImageInversionRequest.class, imageInversionRequest -> {
                    boolean success = imageInverter.invertImage(imageInversionRequest.source,
                            imageInversionRequest.destination);
                    sender().tell(new ImageInverterActorProtocol.ImageInversionResponse(success,
                            imageInversionRequest.source, imageInversionRequest.destination), self());
                })
                .build();
    }
}
