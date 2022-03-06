package com.yonatankarp.recipeapp.controllers;

import com.yonatankarp.recipeapp.services.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeService recipeService;

    @RequestMapping("/recipe/show/{id}")
    public String showById(@PathVariable Long id, final Model model) {

        model.addAttribute("recipe", recipeService.findById(id));

        return "recipe/show";
    }
}
