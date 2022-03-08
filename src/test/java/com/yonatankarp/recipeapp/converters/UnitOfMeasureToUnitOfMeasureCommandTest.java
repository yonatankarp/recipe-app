package com.yonatankarp.recipeapp.converters;

import com.yonatankarp.recipeapp.model.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SuppressWarnings("ConstantConditions")
class UnitOfMeasureToUnitOfMeasureCommandTest {

    private static final String DESCRIPTION = "description";
    private static final Long UOM_ID = 1L;

    private UnitOfMeasureToUnitOfMeasureCommand converter;

    @BeforeEach
    void setUp() throws Exception {
        converter = new UnitOfMeasureToUnitOfMeasureCommand();
    }

    @Test
    void testNullObjectConvert() {
        assertNull(converter.convert(null));
    }

    @Test
    void testEmptyObj() {
        assertNotNull(converter.convert(new UnitOfMeasure()));
    }

    @Test
    void convert() throws Exception {
        //given
        final var unitOfMeasure = UnitOfMeasure.builder()
                .id(UOM_ID)
                .description(DESCRIPTION)
                .build();

        //when
        final var unitOfMeasureCommand = converter.convert(unitOfMeasure);

        //then
        assertNotNull(unitOfMeasureCommand);
        assertEquals(UOM_ID, unitOfMeasureCommand.getId());
        assertEquals(DESCRIPTION, unitOfMeasureCommand.getDescription());
    }
}
