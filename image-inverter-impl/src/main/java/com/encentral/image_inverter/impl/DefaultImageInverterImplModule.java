package com.encentral.image_inverter.impl;

import com.encentral.image_inverter.api.ImageInverter;
import com.google.inject.AbstractModule;

public class DefaultImageInverterImplModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ImageInverter.class).to(DefaultImageInverterImpl.class);
    }
}
