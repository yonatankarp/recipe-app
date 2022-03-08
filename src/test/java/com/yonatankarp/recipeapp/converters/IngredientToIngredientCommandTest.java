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

    private static final Recipe RECIPE = new Recipe();
    private static final BigDecimal AMOUNT = new BigDecimal("1");
    private static final String DESCRIPTION = "Cheeseburger";
    private static final Long UOM_ID = 2L;
    private static final Long ID_VALUE = 1L;

    private IngredientToIngredientCommand converter;

    @BeforeEach
    public void setUp() {
        converter = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
    }

    @Test
    public void testNullConvert() {
        assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObject() {
        assertNotNull(converter.convert(new Ingredient()));
    }

    @Test
    public void testConvertNullUOM() {
        //given
        final var ingredient = Ingredient.builder()
                .id(ID_VALUE)
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
        assertEquals(ID_VALUE, ingredientCommand.getId());
        assertEquals(AMOUNT, ingredientCommand.getAmount());
        assertEquals(DESCRIPTION, ingredientCommand.getDescription());
    }

    @Test
    public void testConvertWithUom() {
        //given
        final var unitOfMeasure = UnitOfMeasure.builder().id(UOM_ID).build();

        final var ingredient = Ingredient.builder()
                .id(ID_VALUE)
                .recipe(RECIPE)
                .amount(AMOUNT)
                .description(DESCRIPTION)
                .uom(unitOfMeasure)
                .build();

        //when
        final var ingredientCommand = converter.convert(ingredient);

        //then
        assertNotNull(ingredientCommand);
        assertEquals(ID_VALUE, ingredientCommand.getId());
        assertNotNull(ingredientCommand.getUom());
        assertEquals(UOM_ID, ingredientCommand.getUom().getId());
        assertEquals(AMOUNT, ingredientCommand.getAmount());
        assertEquals(DESCRIPTION, ingredientCommand.getDescription());
    }
}