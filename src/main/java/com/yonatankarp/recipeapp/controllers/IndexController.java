package com.yonatankarp.recipeapp.controllers;

import com.yonatankarp.recipeapp.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class IndexController {

    private final RecipeService recipeService;

    public IndexController(final RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping({"", "/", "/index", "/index.html"})
    public String getIndexPage(final Model model) {
        log.debug("Getting index page...");

        model.addAttribute("recipes", recipeService.getRecipes());

        return "index";
    }
}
