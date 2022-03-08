package com.yonatankarp.recipeapp.converters;

import com.yonatankarp.recipeapp.commands.CategoryCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SuppressWarnings("ConstantConditions")
class CategoryCommandToCategoryTest {

    private static final Long ID_VALUE = 1L;
    private static final String DESCRIPTION = "description";

    private CategoryCommandToCategory converter;

    @BeforeEach
    void setUp() {
        converter = new CategoryCommandToCategory();
    }

    @Test
    void testNullObject() {
        assertNull(converter.convert(null));
    }

    @Test
    void testEmptyObject() {
        assertNotNull(converter.convert(new CategoryCommand()));
    }

    @Test
    void convert() {
        //given
        final var categoryCommand = CategoryCommand.builder()
                .id(ID_VALUE)
                .description(DESCRIPTION)
                .build();

        //when
        final var category = converter.convert(categoryCommand);

        //then
        assertNotNull(category);
        assertEquals(ID_VALUE, category.getId());
        assertEquals(DESCRIPTION, category.getDescription());
    }
}
