package com.yonatankarp.recipeapp.services;

import com.yonatankarp.recipeapp.commands.IngredientCommand;
import com.yonatankarp.recipeapp.converters.IngredientCommandToIngredient;
import com.yonatankarp.recipeapp.converters.IngredientToIngredientCommand;
import com.yonatankarp.recipeapp.exceptions.NotFoundException;
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

        final var ingredientOptional = ingredientRepository.findByRecipeIdAndId(recipeId, ingredientId);

        if (ingredientOptional.isEmpty()) {
            log.error("Ingredient not found for id: {}", ingredientId);
            throw new NotFoundException("Ingredient not found for id" + ingredientId);
        }

        return ingredientToIngredientCommand.convert(ingredientOptional.get());
    }

    @Transactional
    @Override
    public IngredientCommand saveIngredientCommand(final IngredientCommand command) {
        final var recipeOptional = recipeRepository.findById(command.getRecipeId());
        if (recipeOptional.isEmpty()) {
            log.error("Recipe not found for id {}", command.getRecipeId());
            throw new NotFoundException("Recipe not found for id: " + command.getRecipeId());
        }

        final var recipe = recipeOptional.get();

        final var ingredientOptional = ingredientRepository.findByRecipeIdAndId(command.getRecipeId(), command.getId());
        if (ingredientOptional.isPresent()) {
            final var ingredientFound = ingredientOptional.get();
            ingredientFound.setDescription(command.getDescription());
            ingredientFound.setAmount(command.getAmount());
            ingredientFound.setUom(unitOfMeasureRepository.findById(command.getUom().getId())
                    .orElseThrow(() -> new NotFoundException("Unit of measure not found")));
        } else {
            final var ingredient = ingredientCommandToIngredient.convert(command);
            if(ingredient != null) {
                ingredient.setRecipe(recipe);
                recipe.getIngredients().add(ingredient);
            }
        }

        final var savedRecipe = recipeRepository.save(recipe);

        var savedIngredientOptional = savedRecipe.getIngredients().stream()
                .filter(ingredient ->  ingredient.getId().equals(command.getId()))
                .findFirst();

        // If this was not persist we cannot use the id, therefore we're using best guess to match the ingredient
        if(savedIngredientOptional.isEmpty()) {
            savedIngredientOptional = savedRecipe.getIngredients().stream()
                    .filter(ingredient -> ingredient.getDescription().equalsIgnoreCase(command.getDescription()))
                    .filter(ingredient -> ingredient.getAmount().equals(command.getAmount()))
                    .filter(ingredient -> ingredient.getUom().getId().equals(command.getUom().getId()))
                    .findFirst();
        }

        if(savedIngredientOptional.isEmpty()) {
            log.error("No ingredient was found to update");
            throw new NotFoundException("No ingredient was found to update");
        }

        return ingredientToIngredientCommand.convert(savedIngredientOptional.get());
    }

    @Transactional
    @Override
    public void deleteByRecipeIdAndIngredientId(final Long recipeId, final Long ingredientId) {
        ingredientRepository.deleteByRecipeIdAndId(recipeId, ingredientId);
    }
}
