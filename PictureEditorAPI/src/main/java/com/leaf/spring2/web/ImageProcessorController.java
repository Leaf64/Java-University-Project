package com.leaf.spring2.web;

import com.google.gson.JsonObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;


public class ImageProcessorController {


    public static Map<String, BufferedImage> map = new HashMap<String, BufferedImage>();
    private static int id;


    public static boolean exists(int id) { //if image of given id is not existing then throw 404 exception
        if (map.get(Integer.toString(id)) != null) {
            return true;
        } else {
            throw new NotFoundException();
        }
    }

    public static byte[] toBytes(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();
        return imageInByte;
    }

    public static JsonObject setImage(InputStream imgBin) throws IOException {

        BufferedImage imgBuf = ImageIO.read(imgBin);
        if (imgBin == null) {
            System.err.println("CHUJ");
        }
        JsonObject imgInfo = new JsonObject();
        imgInfo.addProperty("HEIGHT", imgBuf.getHeight());
        imgInfo.addProperty("WIDTH", imgBuf.getWidth());
        id++;
        imgInfo.addProperty("ID", id);
        map.put(Integer.toString(id), imgBuf);
        return imgInfo;
    }

    public static boolean deleteImage(int id) {
        System.err.println("Image processor id: " + id);

        if (map.get(Integer.toString(id)) != null) {
            try {
                map.remove(Integer.toString(id));
                return true;
            } catch (NullPointerException ex) {
                return false;
            }
        } else {
            return false;
        }
    }

    public static JsonObject getImageInfo(int id) {
        if (map.get(Integer.toString(id)) != null) {
            try {
                BufferedImage imgBuf = map.get(Integer.toString(id));
                JsonObject imgInfo = new JsonObject();
                imgInfo.addProperty("HEIGHT", imgBuf.getHeight());
                imgInfo.addProperty("WIDTH", imgBuf.getWidth());
                imgInfo.addProperty("ID", id);
                return imgInfo;

            } catch (NullPointerException ex) {
                throw new NotFoundException();
            }
        } else {
            throw new NotFoundException();
        }
    }

    public static byte[] getScaledImage(int id, int percentInt) throws IOException {

        exists(id);
        double percent = (double) percentInt;
        BufferedImage imgBuf = map.get(Integer.toString(id));
        BufferedImage scaledImg = new BufferedImage((int) (imgBuf.getWidth() * (percent / 100)), (int) (imgBuf.getHeight() * (percent / 100)), BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = new AffineTransform();
        at.scale(percent / 100, percent / 100);
        AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        scaledImg = scaleOp.filter(imgBuf, scaledImg);

        return toBytes(scaledImg);

    }

    public static JsonObject getHistogram(int id) {
        exists(id);
        JsonObject histo = new JsonObject();
        JsonObject red = new JsonObject();
        JsonObject green = new JsonObject();
        JsonObject blue = new JsonObject();

        double redtab[] = new double[256];
        double greentab[] = new double[256];
        double bluetab[] = new double[256];

        BufferedImage imgBuf = map.get(Integer.toString(id));


        for (int i = 0; i < imgBuf.getWidth(); i++) {
            for (int j = 0; j < imgBuf.getHeight(); j++) {
                Color c = new Color(imgBuf.getRGB(i, j));
                redtab[c.getRed()]++;
                greentab[c.getGreen()]++;
                bluetab[c.getBlue()]++;
            }
        }
        int sumred = 0;
        int sumgreen = 0;
        int sumblue = 0;
        for (int i = 0; i < 256; i++) {
            sumred += redtab[i];
            sumgreen += greentab[i];
            sumblue += bluetab[i];
        }

        for (int i = 0; i < 256; i++) {
            redtab[i] = redtab[i] / sumred;
            greentab[i] = greentab[i] / sumgreen;
            bluetab[i] = bluetab[i] / sumblue;
        }
        for (int i = 0; i < 256; i++) {
            red.addProperty(Integer.toString(i), redtab[i]);
            green.addProperty(Integer.toString(i), greentab[i]);
            blue.addProperty(Integer.toString(i), bluetab[i]);
        }

        histo.add("Red", red);
        histo.add("Green", green);
        histo.add("Blue", blue);

        return histo;
    }

    public static byte[] cropImage(int id, int start, int stop, int width, int height) throws IOException {
        exists(id);
        BufferedImage imgBuf = map.get(Integer.toString(id));
        BufferedImage dest = imgBuf.getSubimage(0, 0, width, height);

        return toBytes(dest);
    }

    public static byte[] getGreyImage(int id) throws IOException {
        exists(id);
        BufferedImage imgBuf = map.get(Integer.toString(id));
        BufferedImage image = new BufferedImage(imgBuf.getWidth(), imgBuf.getHeight(), BufferedImage.TYPE_INT_ARGB);

        try {
            int width = image.getWidth();
            int height = image.getHeight();

            for (int i = 0; i < height; i++) {

                for (int j = 0; j < width; j++) {

                    Color c = new Color(imgBuf.getRGB(j, i));
                    int red = (int) (c.getRed() * 0.299);
                    int green = (int) (c.getGreen() * 0.587);
                    int blue = (int) (c.getBlue() * 0.114);
                    Color newColor = new Color(red + green + blue,

                            red + green + blue, red + green + blue);

                    image.setRGB(j, i, newColor.getRGB());
                }
            }
        } catch (Exception e) {
        }

        //converting to bytes
        return toBytes(image);
    }

    public static byte[] getBlurredImage(int id, int radius) throws IOException {
        exists(id);
        BufferedImage imgBuf = map.get(Integer.toString(id));

        BufferedImage image = blur(imgBuf, radius);

        //converting to bytes
        return toBytes(image);

    }

    public static BufferedImage blur(BufferedImage image, int filterWidth) {

        int[] filter = new int[filterWidth];
        int keki = 0;
        for (int i = 0; i < filterWidth; i++) {
            if (i < filterWidth / 2) {
                keki++;
            } else {
                keki--;
            }
            filter[i] = keki;
        }


        //=====================================================================


        if (filter.length % filterWidth != 0) {
            throw new IllegalArgumentException("filter contains a incomplete row");
        }

        final int width = image.getWidth();
        final int height = image.getHeight();
        final int sum = IntStream.of(filter).sum();

        int[] input = image.getRGB(0, 0, width, height, null, 0, width);

        int[] output = new int[input.length];

        final int pixelIndexOffset = width - filterWidth;
        final int centerOffsetX = filterWidth / 2;
        final int centerOffsetY = filter.length / filterWidth / 2;

        // apply filter
        for (int h = height - filter.length / filterWidth + 1, w = width - filterWidth + 1, y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int r = 0;
                int g = 0;
                int b = 0;
                for (int filterIndex = 0, pixelIndex = y * width + x;
                     filterIndex < filter.length;
                     pixelIndex += pixelIndexOffset) {
                    for (int fx = 0; fx < filterWidth; fx++, pixelIndex++, filterIndex++) {
                        int col = input[pixelIndex];
                        int factor = filter[filterIndex];

                        // sum up color channels seperately
                        r += ((col >>> 16) & 0xFF) * factor;
                        g += ((col >>> 8) & 0xFF) * factor;
                        b += (col & 0xFF) * factor;
                    }
                }
                r /= sum;
                g /= sum;
                b /= sum;
                // combine channels with full opacity
                output[x + centerOffsetX + (y + centerOffsetY) * width] = (r << 16) | (g << 8) | b | 0xFF000000;
            }
        }

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        result.setRGB(0, 0, width, height, output, 0, width);
        return result;
    }
}
