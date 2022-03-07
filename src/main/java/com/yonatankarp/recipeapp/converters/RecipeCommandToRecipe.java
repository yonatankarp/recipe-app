package com.yonatankarp.recipeapp.converters;

import java.util.stream.Collectors;
import com.yonatankarp.recipeapp.commands.RecipeCommand;
import com.yonatankarp.recipeapp.model.Recipe;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@SuppressWarnings("ConstantConditions")
public class RecipeCommandToRecipe implements Converter<RecipeCommand, Recipe> {

    private final CategoryCommandToCategory categoryCommandToCategory;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final NotesCommandToNotes notesCommandToNotes;

    @Synchronized
    @Nullable
    @Override
    public Recipe convert(final RecipeCommand source) {
        if(source == null) {
            return null;
        }

        final var categories = source.getCategories().stream()
                .map(categoryCommandToCategory::convert)
                .collect(Collectors.toSet());

        final var ingredients = source.getIngredients().stream()
                .map(ingredientCommandToIngredient::convert)
                .collect(Collectors.toSet());

        return Recipe.builder()
                .id(source.getId())
                .description(source.getDescription())
                .difficulty(source.getDifficulty())
                .prepTime(source.getPrepTime())
                .cookTime(source.getCookTime())
                .servings(source.getServings())
                .source(source.getSource())
                .url(source.getUrl())
                .directions(source.getDirections())
                .categories(categories)
                .ingredients(ingredients)
                .notes(notesCommandToNotes.convert(source.getNotes()))
                .build();
    }
}
