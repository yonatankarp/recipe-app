package com.yonatankarp.recipeapp.services;

import com.yonatankarp.recipeapp.commands.IngredientCommand;
import com.yonatankarp.recipeapp.converters.IngredientCommandToIngredient;
import com.yonatankarp.recipeapp.converters.IngredientToIngredientCommand;
import com.yonatankarp.recipeapp.exceptions.UnitOfMeasureNotFoundException;
import com.yonatankarp.recipeapp.repositories.IngredientRepository;
import com.yonatankarp.recipeapp.repositories.RecipeRepository;
import com.yonatankarp.recipeapp.repositories.UnitOfMeasureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {

        final var ingredientOptional = ingredientRepository.findByIdAndRecipeId(ingredientId, recipeId);

        if (ingredientOptional.isEmpty()) {
            // TODO: implement error handling
            log.error("Ingredient id not found: {}", ingredientId);
        }

        return ingredientToIngredientCommand.convert(ingredientOptional.get());
    }

    @Transactional
    @Override
    public IngredientCommand saveIngredientCommand(final IngredientCommand command) {
        final var recipeOptional = recipeRepository.findById(command.getRecipeId());
        if (recipeOptional.isEmpty()) {
            // TODO: error handling
            log.error("Recipe not found for id {}", command.getRecipeId());
            return new IngredientCommand();
        }

        final var recipe = recipeOptional.get();

        final var ingredientOptional = ingredientRepository.findByIdAndRecipeId(command.getId(), command.getRecipeId());
        if (ingredientOptional.isPresent()) {
            final var ingredientFound = ingredientOptional.get();
            ingredientFound.setDescription(command.getDescription());
            ingredientFound.setAmount(command.getAmount());
            ingredientFound.setUom(unitOfMeasureRepository.findById(command.getUom().getId())
                    .orElseThrow(() -> new UnitOfMeasureNotFoundException("Unit of measure not found")));
        } else {
            recipe.getIngredients().add(ingredientCommandToIngredient.convert(command));
        }

        final var savedRecipe = recipeRepository.save(recipe);

        return ingredientToIngredientCommand.convert(savedRecipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(command.getId()))
                .findFirst()
                .get());
    }
}
