package com.yonatankarp.recipeapp.exceptions;

public class UnitOfMeasureNotFoundException extends RuntimeException {
    public UnitOfMeasureNotFoundException(final String message) {
        super(message);
    }
}
