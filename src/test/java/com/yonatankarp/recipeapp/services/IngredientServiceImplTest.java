package com.yonatankarp.recipeapp.services;

import java.math.BigDecimal;
import java.util.Optional;
import com.yonatankarp.recipeapp.commands.IngredientCommand;
import com.yonatankarp.recipeapp.commands.UnitOfMeasureCommand;
import com.yonatankarp.recipeapp.converters.IngredientCommandToIngredient;
import com.yonatankarp.recipeapp.converters.IngredientToIngredientCommand;
import com.yonatankarp.recipeapp.converters.UnitOfMeasureCommandToUnitOfMeasure;
import com.yonatankarp.recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.yonatankarp.recipeapp.exceptions.NotFoundException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    void findByRecipeIdAndIngredientIdIngredientNotFound() {
        when(ingredientRepository.findByRecipeIdAndId(anyLong(), anyLong())).thenReturn(Optional.empty());

        final var exception = assertThrows(NotFoundException.class, () ->
                ingredientService.findByRecipeIdAndIngredientId(RECIPE_ID, INGREDIENT_ID));


        assertTrue(exception.getMessage().contains("Ingredient not found"));
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
    void saveRecipeCommandRecipeNotFound() {
        final var command = IngredientCommand.builder().build();

        when(recipeRepository.findById(anyLong())).thenReturn(Optional.empty());

        final var exception = assertThrows(NotFoundException.class, () ->
                ingredientService.saveIngredientCommand(command));

        assertTrue(exception.getMessage().contains("Recipe not found"));
    }

    @Test
    void saveRecipeCommandUOMNotFound() {
        final var command = IngredientCommand.builder()
                .id(RECIPE_ID)
                .recipeId(RECIPE_ID)
                .uom(UnitOfMeasureCommand.builder().id(UOM_ID).build())
                .build();

        final var recipe = Recipe.builder().id(RECIPE_ID).build();
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe));

        final var ingredient = Ingredient.builder().id(INGREDIENT_ID).build();
        when(ingredientRepository.findByRecipeIdAndId(anyLong(), anyLong())).thenReturn(Optional.of(ingredient));

        when(unitOfMeasureRepository.findById(anyLong())).thenReturn(Optional.empty());

        final var exception = assertThrows(NotFoundException.class, () ->
                ingredientService.saveIngredientCommand(command));

        assertTrue(exception.getMessage().contains("Unit of measure not found"));
    }

    @Test
    void saveRecipeCommandIngredientToUpdateNotFound() {

        final var command = IngredientCommand.builder()
                .id(RECIPE_ID)
                .recipeId(RECIPE_ID)
                .uom(UnitOfMeasureCommand.builder().id(UOM_ID).build())
                .build();

        final var ingredient = Ingredient.builder().id(INGREDIENT_ID).build();
        when(ingredientRepository.findByRecipeIdAndId(anyLong(), anyLong())).thenReturn(Optional.of(ingredient));

        final var unitOfMeasure = UnitOfMeasure.builder().id(UOM_ID).build();
        when(unitOfMeasureRepository.findById(anyLong())).thenReturn(Optional.of(unitOfMeasure));

        final var recipe = Recipe.builder().id(RECIPE_ID).ingredient(ingredient).build();
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe));

        final var savedRecipe = Recipe.builder()
                .id(2L)
                .ingredient(Ingredient.builder()
                        .id(2L)
                        .description("random description")
                        .amount(BigDecimal.ZERO)
                        .uom(UnitOfMeasure.builder().build())
                        .build())
                .build();

        when(recipeRepository.save(any())).thenReturn(savedRecipe);


        final var exception = assertThrows(NotFoundException.class, () ->
                ingredientService.saveIngredientCommand(command));

        assertTrue(exception.getMessage().contains("No ingredient was found"));
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

        final var ingredient = Ingredient.builder()
                .id(INGREDIENT_ID)
                .description("A nice description")
                .amount(BigDecimal.TEN)
                .uom(UnitOfMeasure.builder().id(UOM_ID).build())
                .build();

        final var savedRecipe = Recipe.builder()
                .ingredient(ingredient)
                .build();

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
