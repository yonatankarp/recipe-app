package com.yonatankarp.recipeapp.services;

import java.io.IOException;
import java.util.Arrays;
import com.yonatankarp.recipeapp.repositories.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final RecipeRepository recipeRepository;

    @Override
    public void saveImageFile(Long recipeId, MultipartFile file) {
        log.debug("Received a file to store");

        try {
            final var recipeOptional = recipeRepository.findById(recipeId);

            if(recipeOptional.isEmpty()) {
                // TODO: error handling
                log.error("Could not find recipe with id {} to store image for.", recipeId);
            }

            final var recipe = recipeOptional.get();

            final var byteObject = Arrays.copyOf(file.getBytes(), file.getBytes().length);

            recipe.setImage(byteObject);

            recipeRepository.save(recipe);

        } catch (final IOException e) {
            // TODO: error handling
            log.error("Failed to store image, caused by: {}", e.getMessage());
        }
    }
}
