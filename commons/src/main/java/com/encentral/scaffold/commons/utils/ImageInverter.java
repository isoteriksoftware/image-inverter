package com.encentral.scaffold.commons.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageInverter {
    public boolean invertImage(File source, File destination) {
        if (source == null)
            throw new NullPointerException("Source file should not be null!");

        if (destination == null)
            throw new NullPointerException("Destination file should not be null!");

        if (!source.exists())
            throw new IllegalStateException("Source file does not exist!");

        BufferedImage bufferedImage;
        int width, height;

        try {
            bufferedImage = ImageIO.read(source);
            width = bufferedImage.getWidth();
            height = bufferedImage.getHeight();

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = bufferedImage.getRGB(x, y);
                    int a = (rgb>>24)&0xff;
                    int r = (rgb>>16)&0xff;
                    int g = (rgb>>8)&0xff;
                    int b = rgb&0xff;

                    rgb = (a<<24) | (g <<16) | (b <<8) | r;
                    bufferedImage.setRGB(x, y, rgb);
                }
            }

            ImageIO.write(bufferedImage, "jpg", destination);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}






















