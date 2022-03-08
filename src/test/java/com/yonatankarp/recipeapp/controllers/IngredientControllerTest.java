package com.yonatankarp.recipeapp.controllers;

import com.yonatankarp.recipeapp.commands.RecipeCommand;
import com.yonatankarp.recipeapp.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class IngredientControllerTest {

    @Mock
    private RecipeService recipeService;

    private IngredientController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        controller = new IngredientController(recipeService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testListIngredients() throws Exception {
        // Given
        final var command = new RecipeCommand();
        when(recipeService.findCommandById(anyLong())).thenReturn(command);

        // When
        mockMvc.perform(get("/recipe/1/ingredients"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/list"))
                .andExpect(model().attributeExists("recipe"));

        // Then
        verify(recipeService).findCommandById(1L);
    }
}
