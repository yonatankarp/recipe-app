package com.yonatankarp.recipeapp.repositories;

import java.util.Optional;
import com.yonatankarp.recipeapp.model.Ingredient;
import org.springframework.data.repository.CrudRepository;

public interface IngredientRepository extends CrudRepository<Ingredient, Long> {
    Optional<Ingredient> findByRecipeIdAndId(final Long recipeId, final Long ingredientId);

    void deleteByRecipeIdAndId(final Long recipeId, final Long ingredientId);
}
