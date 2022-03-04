package com.yonatankarp.recipeapp.repositories;

import com.yonatankarp.recipeapp.model.Recipe;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {
}
