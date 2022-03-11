package com.yonatankarp.recipeapp.model;

public enum Difficulty {
    EASY("Easy"),
    MODERATE("Moderate"),
    KIND_OF_HARD("Kind of Hard"),
    HARD("Hard");

    private final String value;

    Difficulty(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
