package com.yonatankarp.recipeapp.controllers;

import com.yonatankarp.recipeapp.services.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    private final RecipeService recipeService;

    public IndexController(final RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping({"", "/", "/index", "/index.html"})
    public String getIndexPage(final Model model) {
        model.addAttribute("recipes", recipeService.getRecipes());

        return "index";
    }
}
