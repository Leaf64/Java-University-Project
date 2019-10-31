package com.leaf.spring2.web;


import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.IOException;

@RestController
public class Spring2Controller {
    @RequestMapping("/hello")
    public String sayHello(@RequestParam(value = "name") String name) {
        return "Hello " + name + "!";
    }

    @RequestMapping(value = "/image/add", method = RequestMethod.POST)
    public String addImage(HttpServletRequest requestEntity) throws Exception {
        JsonObject imgInfo = ImageProcessorController.setImage(requestEntity.getInputStream());
        return imgInfo.toString();
    }

    @RequestMapping(value = "/image/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteImage(@PathVariable("id") @NotEmpty int id) {
        System.err.println("Spring controller id: " + id);
        boolean good = ImageProcessorController.deleteImage(id);
        if (good) {
            return ResponseEntity.ok(HttpStatus.OK);
        } else {
            throw new NotFoundException();
        }
    }

    @RequestMapping(value = "/image/{id}/size", method = RequestMethod.GET)
    public String getImageInfo(@PathVariable("id") @NotEmpty int id) {
        JsonObject imgInfo = ImageProcessorController.getImageInfo(id);
        return imgInfo.toString();
    }

    @RequestMapping(value = "/image/{id}/scale/{percent}", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getScaledImage(@PathVariable("id") @NotEmpty int id, @PathVariable("percent") @NotBlank @NotEmpty int percent) throws Exception {
        return ImageProcessorController.getScaledImage(id, percent);
    }

    @RequestMapping(value = "/image/{id}/histogram", method = RequestMethod.GET)
    public String getIHistogram(@PathVariable("id") @NotEmpty int id) {
        JsonObject histogram = ImageProcessorController.getHistogram(id);
        return histogram.toString();
    }

    @RequestMapping(value = "/image/{id}/crop/{start}/{stop}/{width}/{height}", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] cropImage(@PathVariable("id") @NotEmpty int id, @PathVariable("start") @NotEmpty int start,
                            @PathVariable("stop") @NotEmpty int stop, @PathVariable("width") @NotEmpty int width,
                            @PathVariable("height") @NotEmpty int height) throws IOException {

        return ImageProcessorController.cropImage(id, start, stop, width, height);
    }

    @RequestMapping(value = "/image/{id}/greyscale", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getGrayImage(@PathVariable("id") @NotEmpty int id) throws Exception {
        return ImageProcessorController.getGreyImage(id);
    }

    @RequestMapping(value = "/image/{id}/blur/{radius}", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getBlurredImage(@PathVariable("id") @NotEmpty int id, @PathVariable("radius") @NotBlank @Size(max = 100) @NotEmpty int radius) throws Exception {
        return ImageProcessorController.getBlurredImage(id, radius);
    }

}
