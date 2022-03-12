package com.yonatankarp.recipeapp.services;

import java.util.Optional;
import com.yonatankarp.recipeapp.exceptions.NotFoundException;
import com.yonatankarp.recipeapp.model.Recipe;
import com.yonatankarp.recipeapp.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ImageServiceImplTest {

    private static final Long RECIPE_ID = 1L;

    @Mock
    private RecipeRepository recipeRepository;

    private ImageService imageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        imageService = new ImageServiceImpl(recipeRepository);
    }

    @Test
    void saveImageFile() throws Exception {
        // Given
        final var multipartFile = new MockMultipartFile("imagefile", "testing.txt",
                "text/plain", "Recipe App Test".getBytes());

        final var recipe = Recipe.builder().id(RECIPE_ID).build();

        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe));

        final var argumentCapture = ArgumentCaptor.forClass(Recipe.class);

        // When
        imageService.saveImageFile(RECIPE_ID, multipartFile);

        // Then
        verify(recipeRepository).save(argumentCapture.capture());
        final var savedRecipe = argumentCapture.getValue();
        assertEquals(multipartFile.getBytes().length, savedRecipe.getImage().length);
    }

    @Test
    void saveImageFileRecipeNotExist() {
        // Given
        final var multipartFile = new MockMultipartFile("imagefile", "testing.txt",
                "text/plain", "Recipe App Test".getBytes());

        when(recipeRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When
        final var exception = assertThrows(NotFoundException.class, () ->
                imageService.saveImageFile(RECIPE_ID, multipartFile));

        // Then
        assertTrue(exception.getMessage().contains("Recipe not found"));
        verify(recipeRepository, never()).save(any());
    }
}
