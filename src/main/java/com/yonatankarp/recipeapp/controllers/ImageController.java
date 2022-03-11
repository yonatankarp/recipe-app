package com.yonatankarp.recipeapp.controllers;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import com.yonatankarp.recipeapp.services.ImageService;
import com.yonatankarp.recipeapp.services.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;
    private final RecipeService recipeService;

    @GetMapping("/recipe/{recipeId}/image")
    public String showUploadForm(@PathVariable final Long recipeId, final Model model) {
        model.addAttribute("recipe", recipeService.findCommandById(recipeId));

        return "recipe/upload_image_form";
    }

    @PostMapping("recipe/{recipeId}/image")
    public String handleImagePost(@PathVariable final Long recipeId,
                                  @RequestParam("imagefile") MultipartFile file) {

        imageService.saveImageFile(recipeId, file);

        return String.format("redirect:/recipe/%d/show", recipeId);
    }

    @GetMapping("recipe/{recipeId}/recipe_image")
    public void renderImageFromDB(@PathVariable final Long recipeId, final HttpServletResponse response) {
        final var recipeCommand = recipeService.findCommandById(recipeId);

        final var imageByteArray = recipeCommand.getImage();

        if(imageByteArray != null) {

            final var byteArray = Arrays.copyOf(imageByteArray, imageByteArray.length);

            response.setContentType("image/jpeg");

            try (final var inputStream = new ByteArrayInputStream(byteArray)) {
                IOUtils.copy(inputStream, response.getOutputStream());
            } catch (final IOException e) {
                // TODO: error handling
                log.error("Unable to render image of recipe id {}", recipeId);
            }
        }
    }
}
