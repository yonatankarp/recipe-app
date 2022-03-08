package com.yonatankarp.recipeapp.exceptions;

public class RecipeCommandConversionException extends RuntimeException {
    public RecipeCommandConversionException(final String message) {
        super(message);
    }
}
