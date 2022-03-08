package com.yonatankarp.recipeapp.converters;

import java.util.HashSet;
import com.yonatankarp.recipeapp.model.Category;
import com.yonatankarp.recipeapp.model.Difficulty;
import com.yonatankarp.recipeapp.model.Ingredient;
import com.yonatankarp.recipeapp.model.Notes;
import com.yonatankarp.recipeapp.model.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SuppressWarnings("ConstantConditions")
class RecipeToRecipeCommandTest {

    private static final Long RECIPE_ID = 1L;
    private static final Integer COOK_TIME = 5;
    private static final Integer PREP_TIME = 7;
    private static final String DESCRIPTION = "My Recipe";
    private static final String DIRECTIONS = "Directions";
    private static final Difficulty DIFFICULTY = Difficulty.EASY;
    private static final Integer SERVINGS = 3;
    private static final String SOURCE = "Source";
    private static final String URL = "Some URL";
    private static final Long CATEGORY_ID_1 = 1L;
    private static final Long CATEGORY_ID_2 = 2L;
    private static final Long INGREDIENT_ID_1 = 3L;
    private static final Long INGREDIENT_ID_2 = 4L;
    private static final Long NOTES_ID = 9L;

    private RecipeToRecipeCommand converter;

    @BeforeEach
    void setUp() throws Exception {
        converter = new RecipeToRecipeCommand(
                new CategoryToCategoryCommand(),
                new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand()),
                new NotesToNotesCommand());
    }

    @Test
    void testNullObject() {
        assertNull(converter.convert(null));
    }

    @Test
    void testEmptyObject()  {
        assertNotNull(converter.convert(new Recipe()));
    }

    @Test
    void convert() {
        //given
        final var notes = Notes.builder()
                .id(NOTES_ID)
                .build();

        final var categories = new HashSet<Category>();
        categories.add(Category.builder().id(CATEGORY_ID_1).build());
        categories.add(Category.builder().id(CATEGORY_ID_2).build());

        final var ingredients = new HashSet<Ingredient>();
        ingredients.add(Ingredient.builder().id(INGREDIENT_ID_1).build());
        ingredients.add(Ingredient.builder().id(INGREDIENT_ID_2).build());

        final var recipe = Recipe.builder()
                .id(RECIPE_ID)
                .cookTime(COOK_TIME)
                .prepTime(PREP_TIME)
                .description(DESCRIPTION)
                .difficulty(DIFFICULTY)
                .directions(DIRECTIONS)
                .servings(SERVINGS)
                .source(SOURCE)
                .url(URL)
                .notes(notes)
                .categories(categories)
                .ingredients(ingredients)
                .build();

        //when
        final var command = converter.convert(recipe);

        //then
        assertNotNull(command);
        assertEquals(RECIPE_ID, command.getId());
        assertEquals(COOK_TIME, command.getCookTime());
        assertEquals(PREP_TIME, command.getPrepTime());
        assertEquals(DESCRIPTION, command.getDescription());
        assertEquals(DIFFICULTY, command.getDifficulty());
        assertEquals(DIRECTIONS, command.getDirections());
        assertEquals(SERVINGS, command.getServings());
        assertEquals(SOURCE, command.getSource());
        assertEquals(URL, command.getUrl());
        assertEquals(NOTES_ID, command.getNotes().getId());
        assertEquals(2, command.getCategories().size());
        assertEquals(2, command.getIngredients().size());
    }
}
