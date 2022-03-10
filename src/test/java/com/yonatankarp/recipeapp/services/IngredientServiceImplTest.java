package com.yonatankarp.recipeapp.services;

import java.util.Optional;
import com.yonatankarp.recipeapp.converters.IngredientToIngredientCommand;
import com.yonatankarp.recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.yonatankarp.recipeapp.model.Ingredient;
import com.yonatankarp.recipeapp.model.Recipe;
import com.yonatankarp.recipeapp.repositories.IngredientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

class IngredientServiceImplTest {

    private static final Long RECIPE_ID = 3L;
    private static final Long INGREDIENT_ID = 3L;
    private static final Recipe RECIPE = Recipe.builder().id(RECIPE_ID).build();

    private final IngredientToIngredientCommand ingredientToIngredientCommand =
            new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());

    @Mock
    private IngredientRepository ingredientRepository;

    private IngredientService ingredientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ingredientService = new IngredientServiceImpl(ingredientRepository, ingredientToIngredientCommand);
    }

    @Test
    void findByRecipeIdAndIngredientIdHappyPath() {
        // Given
        final var ingredient = Ingredient.builder().id(INGREDIENT_ID).recipe(RECIPE).build();

        when(ingredientRepository.findByIdAndRecipeId(anyLong(), anyLong())).thenReturn(Optional.of(ingredient));

        // When
        final var ingredientCommand = ingredientService.findByRecipeIdAndIngredientId(INGREDIENT_ID, RECIPE_ID);

        // Then
        assertEquals(INGREDIENT_ID, ingredientCommand.getId());
        assertEquals(RECIPE_ID, ingredientCommand.getRecipeId());
    }
}
