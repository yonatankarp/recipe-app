package com.yonatankarp.recipeapp.controllers;

import com.yonatankarp.recipeapp.commands.RecipeCommand;
import com.yonatankarp.recipeapp.exceptions.NotFoundException;
import com.yonatankarp.recipeapp.services.ImageService;
import com.yonatankarp.recipeapp.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class ImageControllerTest {

    private static final Long RECIPE_ID = 1L;

    @Mock
    private ImageService imageService;

    @Mock
    private RecipeService recipeService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        final var controller = new ImageController(imageService, recipeService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    void getImageForm() throws Exception {
        final var command = RecipeCommand.builder().id(RECIPE_ID).build();

        when(recipeService.findCommandById(anyLong())).thenReturn(command);

        mockMvc.perform(get("/recipe/" + RECIPE_ID + "/image"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"));

        verify(recipeService).findCommandById(RECIPE_ID);
    }

    @Test
    void handleImagePost() throws Exception {
        final var multipartFile = new MockMultipartFile("imagefile", "testing.txt",
                "text/plain", "Recipe App Test".getBytes());

        mockMvc.perform(multipart("/recipe/" + RECIPE_ID + "/image").file(multipartFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/recipe/" + RECIPE_ID + "/show"));

        verify(imageService).saveImageFile(RECIPE_ID, multipartFile);
    }

    @Test
    void renderImageFromDB() throws Exception {
        // Given
        final var fileContent = "fake image text".getBytes();

        final var command = RecipeCommand.builder()
                .id(RECIPE_ID)
                .image(fileContent)
                .build();

        when(recipeService.findCommandById(anyLong())).thenReturn(command);

        // When
        final var response = mockMvc.perform(get("/recipe/" + RECIPE_ID + "/recipe_image"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        // Then
        final var responseBytes = response.getContentAsByteArray();
        assertEquals(fileContent.length, responseBytes.length);
    }

    @Test
    void testRecipeNotFound() throws Exception {
        when(recipeService.findCommandById(anyLong())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/recipe/" + RECIPE_ID + "/recipe_image"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("404error"));
    }

    @Test
    void testRecipeBadFormat() throws Exception {
        mockMvc.perform(get("/recipe/asdf/recipe_image"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"));
    }
}
