package com.yonatankarp.recipeapp.services;

import com.yonatankarp.recipeapp.converters.RecipeToRecipeCommand;
import com.yonatankarp.recipeapp.repositories.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class RecipeServiceIntegrationTest {

    private static final String NEW_DESCRIPTION = "New Description";

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeToRecipeCommand recipeToRecipeCommand;

    @Transactional
    @Test
    void testSaveOfDescription() {
        // Given
        final var recipes = recipeRepository.findAll();
        final var testRecipe = recipes.iterator().next();
        final var testRecipeCommand = recipeToRecipeCommand.convert(testRecipe);

        // When
        testRecipeCommand.setDescription(NEW_DESCRIPTION);
        final var savedRecipeCommand = recipeService.saveRecipeCommand(testRecipeCommand);

        // Then
        assertEquals(NEW_DESCRIPTION, savedRecipeCommand.getDescription());
        assertEquals(testRecipe.getId(), savedRecipeCommand.getId());
        assertEquals(testRecipe.getCategories().size(), savedRecipeCommand.getCategories().size());
        assertEquals(testRecipe.getIngredients().size(), savedRecipeCommand.getIngredients().size());
    }
}
