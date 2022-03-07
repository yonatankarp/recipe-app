package com.yonatankarp.recipeapp.services;

import java.util.HashSet;
import java.util.Set;
import com.yonatankarp.recipeapp.commands.RecipeCommand;
import com.yonatankarp.recipeapp.converters.RecipeCommandToRecipe;
import com.yonatankarp.recipeapp.converters.RecipeToRecipeCommand;
import com.yonatankarp.recipeapp.model.Recipe;
import com.yonatankarp.recipeapp.repositories.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;


    public Set<Recipe> getRecipes() {
        log.debug("Fetching recipes...");
        final Set<Recipe> recipeSet = new HashSet<>();
        recipeRepository.findAll().iterator().forEachRemaining(recipeSet::add);
        return recipeSet;
    }

    @Override
    public Recipe findById(Long id) {
        return recipeRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public RecipeCommand saveRecipeCommand(final RecipeCommand command) {
        final var detachedRecipe = recipeCommandToRecipe.convert(command);

        if(detachedRecipe == null) {
            throw new RuntimeException("Recipe command: " + command + " could not be converted");
        }

        final var savedRecipe = recipeRepository.save(detachedRecipe);
        log.debug("Saved Recipe Id {}", savedRecipe.getId());
        return recipeToRecipeCommand.convert(savedRecipe);
    }
}
