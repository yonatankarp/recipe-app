package com.yonatankarp.recipeapp.controllers;

import com.yonatankarp.recipeapp.services.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/recipe")
public class IngredientController {

    private final RecipeService recipeService;

    @GetMapping("/{recipeId}/ingredients")
    public String listIngredients(@PathVariable final Long recipeId, final Model model) {
        log.debug("Getting ingredients for recipe id {}", recipeId);

        // We're using command object to avoid lazy load error in thymeleaf.
        model.addAttribute("recipe", recipeService.findCommandById(recipeId));

        return "recipe/ingredient/list";
    }
}