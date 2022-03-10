package com.yonatankarp.recipeapp.services;

import com.yonatankarp.recipeapp.commands.IngredientCommand;

public interface IngredientService {
    IngredientCommand findByRecipeIdAndIngredientId(final Long recipeId, final Long ingredientId);

    IngredientCommand saveIngredientCommand(final IngredientCommand command);
}
