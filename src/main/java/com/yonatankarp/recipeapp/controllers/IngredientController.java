package com.yonatankarp.recipeapp.controllers;

import com.yonatankarp.recipeapp.commands.IngredientCommand;
import com.yonatankarp.recipeapp.commands.UnitOfMeasureCommand;
import com.yonatankarp.recipeapp.services.IngredientService;
import com.yonatankarp.recipeapp.services.RecipeService;
import com.yonatankarp.recipeapp.services.UnitOfMeasureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/recipe")
public class IngredientController {

    private static final String RECIPE_ATTRIBUTE_NAME = "recipe";
    private static final String INGREDIENT_ATTRIBUTE_NAME = "ingredient";
    private static final String UOM_ATTRIBUTE_NAME = "uomList";

    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;

    @GetMapping("/{recipeId}/ingredients")
    public String listIngredients(@PathVariable final Long recipeId, final Model model) {
        log.debug("Getting ingredients for recipe id {}", recipeId);

        // We're using command object to avoid lazy load error in thymeleaf.
        model.addAttribute(RECIPE_ATTRIBUTE_NAME, recipeService.findCommandById(recipeId));

        return "recipe/ingredient/list";
    }

    @GetMapping("/{recipeId}/ingredient/{ingredientId}/show")
    public String showRecipeIngredient(@PathVariable final Long ingredientId,
                                       @PathVariable final Long recipeId,
                                       final Model model) {
        model.addAttribute(INGREDIENT_ATTRIBUTE_NAME, ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId));

        return "recipe/ingredient/show";
    }

    @GetMapping("/{recipeId}/ingredient/{ingredientId}/update")
    public String updateRecipeIngredient(@PathVariable final Long ingredientId,
                                         @PathVariable final Long recipeId,
                                         final Model model) {
        model.addAttribute(INGREDIENT_ATTRIBUTE_NAME, ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId));

        model.addAttribute(UOM_ATTRIBUTE_NAME, unitOfMeasureService.listAllUoms());

        return "recipe/ingredient/ingredient_form";
    }

    @GetMapping("/{recipeId}/ingredient/{ingredientId}/delete")
    public String deleteRecipeIngredient(@PathVariable final Long ingredientId,
                                         @PathVariable final Long recipeId) {
        log.debug("Deleting ingredient id {} of recipe id {}", ingredientId, recipeId);
        ingredientService.deleteByRecipeIdAndIngredientId(recipeId, ingredientId);
        return String.format("redirect:/recipe/%d/ingredients", recipeId);
    }

    @PostMapping("/{recipeId}/ingredient")
    public String saveOrUpdate(@ModelAttribute final IngredientCommand command) {
        final var savedCommand = ingredientService.saveIngredientCommand(command);

        log.debug("saved recipe id: {}", savedCommand.getRecipeId());
        log.debug("saved ingredient id: {}", savedCommand.getId());

        return String.format("redirect:/recipe/%d/ingredient/%d/show", savedCommand.getRecipeId(), savedCommand.getId());
    }

    @GetMapping("/{recipeId}/ingredient/new")
    public String newIngredient(@PathVariable final Long recipeId, final Model model) {
        // Make sure that we have a valid id value
        final var recipeCommand = recipeService.findCommandById(recipeId);

        if(recipeCommand == null) {
            log.error("Unable to create new ingredient for recipe id {}", recipeId);
        }

        // TODO: error handle if the command is null

        // Need to return parent id for hidden form properties
        final var ingredientCommand = IngredientCommand.builder()
                .recipeId(recipeId)
                .uom(new UnitOfMeasureCommand())
                .build();

        model.addAttribute(INGREDIENT_ATTRIBUTE_NAME, ingredientCommand);

        model.addAttribute(UOM_ATTRIBUTE_NAME, unitOfMeasureService.listAllUoms());

        return "recipe/ingredient/ingredient_form";
    }
}
