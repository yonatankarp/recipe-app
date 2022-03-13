package com.yonatankarp.recipeapp.controllers;

import javax.validation.Valid;
import com.yonatankarp.recipeapp.commands.RecipeCommand;
import com.yonatankarp.recipeapp.services.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

    private static final String VIEWS_RECIPE_FORM = "recipe/recipe_form";
    private static final String VIEWS_RECIPE_SHOW_FORM = "recipe/show";

    private static final String RECIPE_ATTRIBUTE_NAME = "recipe";

    private final RecipeService recipeService;

    @GetMapping("/new")
    public String newRecipe(final Model model) {

        log.debug("Create new recipe");

        model.addAttribute(RECIPE_ATTRIBUTE_NAME, new RecipeCommand());

        return VIEWS_RECIPE_FORM;
    }

    @PostMapping({"", "/"})
    public String saveOrUpdate(@Valid @ModelAttribute("recipe") final RecipeCommand command,
                               final BindingResult bindingResult) {

        log.debug("save or update recipe");

        if(bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> log.error(error.toString()));
            return VIEWS_RECIPE_FORM;
        }

        final var savedCommand = recipeService.saveRecipeCommand(command);

        return String.format("redirect:/recipe/%s/show", savedCommand.getId());
    }

    @GetMapping("/{id}/show")
    public String showById(@PathVariable Long id, final Model model) {

        log.debug("Show recipe with id {}", id);

        model.addAttribute(RECIPE_ATTRIBUTE_NAME, recipeService.findById(id));

        return VIEWS_RECIPE_SHOW_FORM;
    }

    @GetMapping("/{id}/update")
    public String updateRecipe(@PathVariable final Long id, final Model model) {
        log.debug("Update recipe with id {}", id);
        model.addAttribute(RECIPE_ATTRIBUTE_NAME, recipeService.findCommandById(id));
        return VIEWS_RECIPE_FORM;
    }

    @GetMapping("/{id}/delete")
    public String deleteRecipe(@PathVariable final Long id) {
        log.debug("Delete recipe with id {}", id);

        recipeService.deleteById(id);

        return "redirect:/";
    }
}
