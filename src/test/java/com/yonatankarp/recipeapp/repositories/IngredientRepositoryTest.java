package com.yonatankarp.recipeapp.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class IngredientRepositoryTest {

    private static final Long RECIPE_ID = 1L;
    private static final Long INGREDIENT_ID = 3L;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Test
    void findByIdAndRecipeId() {
        final var ingredientOptional = ingredientRepository.findByIdAndRecipeId(INGREDIENT_ID, RECIPE_ID);
        assertTrue(ingredientOptional.isPresent());
        assertEquals(RECIPE_ID, ingredientOptional.get().getRecipe().getId());
        assertEquals(INGREDIENT_ID, ingredientOptional.get().getId());
    }
}
