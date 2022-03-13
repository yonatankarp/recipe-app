package com.yonatankarp.recipeapp.controllers;

import com.yonatankarp.recipeapp.commands.RecipeCommand;
import com.yonatankarp.recipeapp.exceptions.NotFoundException;
import com.yonatankarp.recipeapp.model.Recipe;
import com.yonatankarp.recipeapp.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class RecipeControllerTest {

    private static final Long RECIPE_ID = 1L;
    private static final Long RECIPE_COMMAND_ID = 2L;

    @Mock
    private RecipeService recipeService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        final var controller = new RecipeController(recipeService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    void testGetRecipe() throws Exception {
        final var recipe = Recipe.builder()
                .id(RECIPE_ID)
                .build();

        when(recipeService.findById(any())).thenReturn(recipe);

        mockMvc.perform(get("/recipe/" + RECIPE_ID + "/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/show"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    void testRecipeNotFound() throws Exception {
        when(recipeService.findById(anyLong())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/recipe/" + RECIPE_ID + "/show"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("404error"));
    }

    @Test
    void testRecipeBadFormat() throws Exception {
        mockMvc.perform(get("/recipe/asdf/show"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"));
    }

    @Test
    void testGetNewRecipeForm() throws Exception {
        mockMvc.perform(get("/recipe/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipe_form"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    void testPostNewRecipeForm() throws Exception {
        final var command = RecipeCommand.builder().id(RECIPE_COMMAND_ID).build();

        when(recipeService.saveRecipeCommand(any())).thenReturn(command);

        mockMvc.perform(post("/recipe")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", "")
                        .param("description", "some description")
                        .param("directions", "some directions")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/" + RECIPE_COMMAND_ID + "/show"));
    }

    @Test
    void testPostNewRecipeFormValidationFail() throws Exception {
        final var command = RecipeCommand.builder().id(RECIPE_COMMAND_ID).build();

        when(recipeService.saveRecipeCommand(any())).thenReturn(command);

        mockMvc.perform(post("/recipe")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", "")
                        .param("cookTime", "3000")
                )
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(view().name("recipe/recipe_form"));
    }

    @Test
    void testGetUpdateView() throws Exception {
        final var command = RecipeCommand.builder().id(RECIPE_COMMAND_ID).build();

        when(recipeService.findCommandById(anyLong())).thenReturn(command);

        mockMvc.perform(get("/recipe/" + RECIPE_ID + "/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipe_form"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    void testDeleteAction() throws Exception {
        mockMvc.perform(get("/recipe/" + RECIPE_ID + "/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(recipeService).deleteById(RECIPE_ID);
    }
}
