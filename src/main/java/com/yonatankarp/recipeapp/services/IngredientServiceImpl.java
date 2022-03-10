package com.yonatankarp.recipeapp.services;

import com.yonatankarp.recipeapp.commands.IngredientCommand;
import com.yonatankarp.recipeapp.converters.IngredientToIngredientCommand;
import com.yonatankarp.recipeapp.repositories.IngredientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {

        final var ingredientOptional = ingredientRepository.findByIdAndRecipeId(ingredientId, recipeId);

        if (ingredientOptional.isEmpty()) {
            //todo impl error handling
            log.error("Ingredient id not found: {}", ingredientId);
        }

        return ingredientToIngredientCommand.convert(ingredientOptional.get());
    }
}
