package com.yonatankarp.recipeapp.services;

import java.util.Set;
import com.yonatankarp.recipeapp.model.Recipe;

public interface RecipeService {
    Set<Recipe> getRecipes();
}
