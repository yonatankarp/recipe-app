package com.yonatankarp.recipeapp.converters;

import java.math.BigDecimal;
import com.yonatankarp.recipeapp.model.Ingredient;
import com.yonatankarp.recipeapp.model.Recipe;
import com.yonatankarp.recipeapp.model.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SuppressWarnings("ConstantConditions")
class IngredientToIngredientCommandTest {

    private static final Long RECIPE_ID = 1L;
    private static final Long UOM_ID = 2L;
    private static final Long INGREDIENT_ID = 1L;
    private static final Recipe RECIPE = Recipe.builder().id(RECIPE_ID).build();
    private static final BigDecimal AMOUNT = new BigDecimal("1");
    private static final String DESCRIPTION = "Cheeseburger";

    private IngredientToIngredientCommand converter;

    @BeforeEach
    void setUp() {
        converter = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
    }

    @Test
    void testNullConvert() {
        assertNull(converter.convert(null));
    }

    @Test
    void testEmptyObject() {
        assertNotNull(converter.convert(new Ingredient()));
    }

    @Test
    void testConvertNullUOM() {
        //given
        final var ingredient = Ingredient.builder()
                .id(INGREDIENT_ID)
                .recipe(RECIPE)
                .amount(AMOUNT)
                .description(DESCRIPTION)
                .uom(null)
                .build();

        //when
        final var ingredientCommand = converter.convert(ingredient);

        //then
        assertNotNull(ingredientCommand);
        assertNull(ingredientCommand.getUom());
        assertEquals(INGREDIENT_ID, ingredientCommand.getId());
        assertEquals(AMOUNT, ingredientCommand.getAmount());
        assertEquals(DESCRIPTION, ingredientCommand.getDescription());
    }

    @Test
    void testConvertWithUom() {
        //given
        final var unitOfMeasure = UnitOfMeasure.builder().id(UOM_ID).build();

        final var ingredient = Ingredient.builder()
                .id(INGREDIENT_ID)
                .recipe(RECIPE)
                .amount(AMOUNT)
                .description(DESCRIPTION)
                .uom(unitOfMeasure)
                .build();

        //when
        final var ingredientCommand = converter.convert(ingredient);

        //then
        assertNotNull(ingredientCommand);
        assertEquals(INGREDIENT_ID, ingredientCommand.getId());
        assertNotNull(ingredientCommand.getUom());
        assertEquals(UOM_ID, ingredientCommand.getUom().getId());
        assertEquals(AMOUNT, ingredientCommand.getAmount());
        assertEquals(DESCRIPTION, ingredientCommand.getDescription());
    }
}
