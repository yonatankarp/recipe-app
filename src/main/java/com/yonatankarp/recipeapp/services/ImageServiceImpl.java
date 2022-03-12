package com.yonatankarp.recipeapp.services;

import java.util.Arrays;
import com.yonatankarp.recipeapp.exceptions.NotFoundException;
import com.yonatankarp.recipeapp.repositories.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final RecipeRepository recipeRepository;

    @Transactional
    @SneakyThrows
    @Override
    public void saveImageFile(Long recipeId, MultipartFile file) {
        log.debug("Received a file to store");

        final var recipeOptional = recipeRepository.findById(recipeId);

        if (recipeOptional.isEmpty()) {
            log.error("Recipe not found for id {} to store image for.", recipeId);
            throw new NotFoundException("Recipe not found for id " + recipeId + " to store image for.");
        }

        final var recipe = recipeOptional.get();

        final var byteObject = Arrays.copyOf(file.getBytes(), file.getBytes().length);

        recipe.setImage(byteObject);

        recipeRepository.save(recipe);
    }
}
