package com.yonatankarp.recipeapp.converters;

import java.util.stream.Collectors;
import com.yonatankarp.recipeapp.commands.RecipeCommand;
import com.yonatankarp.recipeapp.model.Recipe;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@SuppressWarnings("ConstantConditions")
public class RecipeToRecipeCommand implements Converter<Recipe, RecipeCommand> {

    private final CategoryToCategoryCommand categoryToCategoryCommand;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final NotesToNotesCommand notesToNotesCommand;

    @Override
    public RecipeCommand convert(final Recipe source) {
        if(source == null) {
            return null;
        }

        final var categories = source.getCategories().stream()
                .map(categoryToCategoryCommand::convert)
                .collect(Collectors.toSet());

        final var ingredients = source.getIngredients().stream()
                .map(ingredientToIngredientCommand::convert)
                .collect(Collectors.toSet());

        return RecipeCommand.builder()
                .id(source.getId())
                .description(source.getDescription())
                .difficulty(source.getDifficulty())
                .prepTime(source.getPrepTime())
                .cookTime(source.getCookTime())
                .servings(source.getServings())
                .source(source.getSource())
                .url(source.getUrl())
                .directions(source.getDirections())
                .image(source.getImage())
                .categories(categories)
                .ingredients(ingredients)
                .notes(notesToNotesCommand.convert(source.getNotes()))
                .build();
    }
}
