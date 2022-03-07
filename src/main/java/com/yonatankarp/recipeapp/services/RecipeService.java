package com.yonatankarp.recipeapp.services;

import java.util.Set;
import com.yonatankarp.recipeapp.commands.RecipeCommand;
import com.yonatankarp.recipeapp.model.Recipe;

public interface RecipeService {
    Set<Recipe> getRecipes();

    Recipe findById(final Long id);

    RecipeCommand saveRecipeCommand(final RecipeCommand command);
}
