package com.yonatankarp.recipeapp.converters;

import com.yonatankarp.recipeapp.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SuppressWarnings("ConstantConditions")
class CategoryToCategoryCommandTest {

    private static final Long ID_VALUE = 1L;
    private static final String DESCRIPTION = "description";

    private CategoryToCategoryCommand converter;

    @BeforeEach
    void setUp() {
        converter = new CategoryToCategoryCommand();
    }

    @Test
    void testNullObject() {
        assertNull(converter.convert(null));
    }

    @Test
    void testEmptyObject() {
        assertNotNull(converter.convert(new Category()));
    }

    @Test
    void convert() {
        //given
        final var category = Category.builder()
                .id(ID_VALUE)
                .description(DESCRIPTION)
                .build();

        //when
        final var categoryCommand = converter.convert(category);

        //then
        assertNotNull(categoryCommand);
        assertEquals(ID_VALUE, categoryCommand.getId());
        assertEquals(DESCRIPTION, categoryCommand.getDescription());

    }
}
