package com.encentral.image_inverter.api;

import java.io.File;

public interface ImageInverter {
    /**
     * Inverts an image file by changing its RGB colors.
     * <strong>Note: This method does not validate the type of file provided. The provided file should be validated
     * as a valid image file before being passed to this method</strong>
     * @param source the target image file to invert (must be an image file)
     * @param destination the destination file for storing the inverted image
     * @return true if the image was successfully inverted without any errors. false otherwise
     */
    boolean invertImage(File source, File destination);
}
