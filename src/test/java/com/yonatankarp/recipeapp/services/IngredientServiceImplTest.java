package com.yonatankarp.recipeapp.services;

import java.math.BigDecimal;
import java.util.Optional;
import com.yonatankarp.recipeapp.commands.IngredientCommand;
import com.yonatankarp.recipeapp.commands.UnitOfMeasureCommand;
import com.yonatankarp.recipeapp.converters.IngredientCommandToIngredient;
import com.yonatankarp.recipeapp.converters.IngredientToIngredientCommand;
import com.yonatankarp.recipeapp.converters.UnitOfMeasureCommandToUnitOfMeasure;
import com.yonatankarp.recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.yonatankarp.recipeapp.model.Ingredient;
import com.yonatankarp.recipeapp.model.Recipe;
import com.yonatankarp.recipeapp.model.UnitOfMeasure;
import com.yonatankarp.recipeapp.repositories.IngredientRepository;
import com.yonatankarp.recipeapp.repositories.RecipeRepository;
import com.yonatankarp.recipeapp.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class IngredientServiceImplTest {

    private static final Long RECIPE_ID = 3L;
    private static final Long INGREDIENT_ID = 3L;
    private static final Long UOM_ID = 7L;
    private static final Recipe RECIPE = Recipe.builder().id(RECIPE_ID).build();

    private final IngredientToIngredientCommand ingredientToIngredientCommand =
            new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());

    private final IngredientCommandToIngredient ingredientCommandToIngredient =
            new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private UnitOfMeasureRepository unitOfMeasureRepository;

    private IngredientService ingredientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ingredientService = new IngredientServiceImpl(recipeRepository, ingredientRepository, unitOfMeasureRepository, ingredientToIngredientCommand, ingredientCommandToIngredient);
    }

    @Test
    void findByRecipeIdAndIngredientIdHappyPath() {
        // Given
        final var ingredient = Ingredient.builder().id(INGREDIENT_ID).recipe(RECIPE).build();

        when(ingredientRepository.findByRecipeIdAndId(anyLong(), anyLong())).thenReturn(Optional.of(ingredient));

        // When
        final var ingredientCommand = ingredientService.findByRecipeIdAndIngredientId(RECIPE_ID, INGREDIENT_ID);

        // Then
        assertEquals(INGREDIENT_ID, ingredientCommand.getId());
        assertEquals(RECIPE_ID, ingredientCommand.getRecipeId());
    }

    @Test
    void saveRecipeCommand() {
        // Given
        final var command = IngredientCommand.builder()
                .id(INGREDIENT_ID)
                .recipeId(RECIPE_ID)
                .build();

        final var savedRecipe = new Recipe();
        savedRecipe.getIngredients().add(Ingredient.builder().id(INGREDIENT_ID).build());

        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(new Recipe()));
        when(recipeRepository.save(any())).thenReturn(savedRecipe);

        // When
        final var savedCommand = ingredientService.saveIngredientCommand(command);

        // Then
        assertEquals(INGREDIENT_ID, savedCommand.getId());
        verify(recipeRepository).findById(RECIPE_ID);
        verify(recipeRepository).save(any(Recipe.class));
    }

    @Test
    void saveRecipeCommandIngredientNotFound() {
        // Given
        final var command = IngredientCommand.builder()
                .id(15L)
                .recipeId(RECIPE_ID)
                .description("A nice description")
                .amount(BigDecimal.TEN)
                .uom(UnitOfMeasureCommand.builder().id(UOM_ID).build())
                .build();

        final var savedRecipe = new Recipe();
        final var ingredient = Ingredient.builder()
                .id(INGREDIENT_ID)
                .description("A nice description")
                .amount(BigDecimal.TEN)
                .uom(UnitOfMeasure.builder().id(UOM_ID).build())
                .build();
        savedRecipe.getIngredients().add(ingredient);

        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(new Recipe()));
        when(recipeRepository.save(any())).thenReturn(savedRecipe);

        // When
        final var savedCommand = ingredientService.saveIngredientCommand(command);

        // Then
        assertEquals(INGREDIENT_ID, savedCommand.getId());
        verify(recipeRepository).findById(RECIPE_ID);
        verify(recipeRepository).save(any(Recipe.class));
    }

    @Test
    void deleteByRecipeIdAndIngredientId() {
        // Given
        final var idToDelete = 2L;

        // When
        ingredientRepository.deleteByRecipeIdAndId(RECIPE_ID, idToDelete);

        // Then
        verify(ingredientRepository).deleteByRecipeIdAndId(RECIPE_ID, idToDelete);
    }
}
