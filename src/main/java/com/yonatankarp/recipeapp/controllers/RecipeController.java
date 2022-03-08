package com.yonatankarp.recipeapp.controllers;

import com.yonatankarp.recipeapp.commands.RecipeCommand;
import com.yonatankarp.recipeapp.services.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/recipe")
public class RecipeController {
    private final RecipeService recipeService;

    @RequestMapping("/new")
    public String newRecipe(final Model model) {

        log.debug("Create new recipe");

        model.addAttribute("recipe", new RecipeCommand());

        return "recipe/recipe_form";
    }

    @PostMapping({"", "/"})
    public String saveOrUpdate(@ModelAttribute final RecipeCommand command) {
        log.debug("save or update recipe");

        final var savedCommand = recipeService.saveRecipeCommand(command);

        return String.format("redirect:/recipe/%s/show", savedCommand.getId());
    }

    @RequestMapping("/{id}/show")
    public String showById(@PathVariable Long id, final Model model) {

        log.debug("Show recipe with id {}", id);

        model.addAttribute("recipe", recipeService.findById(id));

        return "recipe/show";
    }

    @RequestMapping("/{id}/update")
    public String updateRecipe(@PathVariable final Long id, final Model model) {
        log.debug("Update recipe with id {}", id);
        model.addAttribute("recipe", recipeService.findCommandById(id));
        return "recipe/recipe_form";
    }
}
