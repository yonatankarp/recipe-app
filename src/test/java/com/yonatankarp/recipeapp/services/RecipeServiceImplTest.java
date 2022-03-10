package com.yonatankarp.recipeapp.services;

import java.util.HashSet;
import java.util.Optional;
import com.yonatankarp.recipeapp.converters.RecipeCommandToRecipe;
import com.yonatankarp.recipeapp.converters.RecipeToRecipeCommand;
import com.yonatankarp.recipeapp.model.Recipe;
import com.yonatankarp.recipeapp.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {

    private static final Long RECIPE_ID = 1L;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private RecipeCommandToRecipe recipeCommandToRecipe;

    @Mock
    private RecipeToRecipeCommand recipeToRecipeCommand;

    private RecipeService recipeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        recipeService = new RecipeServiceImpl(recipeRepository, recipeCommandToRecipe, recipeToRecipeCommand);
    }


    @Test
    void getRecipeById() {
        // Given
        final var recipe = Recipe.builder()
                .id(RECIPE_ID)
                .build();
        final var recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

        // When
        final var recipeReturned = recipeService.findById(RECIPE_ID);

        // Then
        assertNotNull(recipeReturned);
        verify(recipeRepository).findById(RECIPE_ID);
        verify(recipeRepository, never()).findAll();
    }

    @Test
    void getRecipes() {
        // Given
        final var recipe = new Recipe();
        final var recipeData = new HashSet<Recipe>();
        recipeData.add(recipe);

        when(recipeRepository.findAll()).thenReturn(recipeData);

        // When
        final var recipes = recipeService.getRecipes();

        // Then
        assertEquals(1, recipes.size());
        verify(recipeRepository, times(1)).findAll();
    }

    @Test
    void testDeleteById() {
        // Given
        final var idToDelete = 2L;

        // When
        recipeService.deleteById(idToDelete);

        // Then
        verify(recipeRepository).deleteById(idToDelete);
    }
}
