package com.yonatankarp.recipeapp.controllers;

import com.yonatankarp.recipeapp.commands.RecipeCommand;
import com.yonatankarp.recipeapp.services.RecipeService;
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
public class RecipeController {

    private static final String RECIPE_ATTRIBUTE_NAME = "recipe";

    private final RecipeService recipeService;

    @GetMapping("/new")
    public String newRecipe(final Model model) {

        log.debug("Create new recipe");

        model.addAttribute(RECIPE_ATTRIBUTE_NAME, new RecipeCommand());

        return "recipe/recipe_form";
    }

    @PostMapping({"", "/"})
    public String saveOrUpdate(@ModelAttribute final RecipeCommand command) {
        log.debug("save or update recipe");

        final var savedCommand = recipeService.saveRecipeCommand(command);

        return String.format("redirect:/recipe/%s/show", savedCommand.getId());
    }

    @GetMapping("/{id}/show")
    public String showById(@PathVariable Long id, final Model model) {

        log.debug("Show recipe with id {}", id);

        model.addAttribute(RECIPE_ATTRIBUTE_NAME, recipeService.findById(id));

        return "recipe/show";
    }

    @GetMapping("/{id}/update")
    public String updateRecipe(@PathVariable final Long id, final Model model) {
        log.debug("Update recipe with id {}", id);
        model.addAttribute(RECIPE_ATTRIBUTE_NAME, recipeService.findCommandById(id));
        return "recipe/recipe_form";
    }

    @GetMapping("/{id}/delete")
    public String deleteRecipe(@PathVariable final Long id) {
        log.debug("Delete recipe with id {}", id);

        recipeService.deleteById(id);

        return "redirect:/";
    }
}
