package com.yonatankarp.recipeapp.converters;

import com.yonatankarp.recipeapp.model.Notes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SuppressWarnings("ConstantConditions")
class NotesToNotesCommandTest {

    private static final Long ID_VALUE = 1L;
    private static final String RECIPE_NOTES = "Notes";

    private NotesToNotesCommand converter;

    @BeforeEach
    void setUp() throws Exception {
        converter = new NotesToNotesCommand();
    }

    @Test
    void convert() {
        //given
        final var notes = Notes.builder()
                .id(ID_VALUE)
                .recipeNotes(RECIPE_NOTES)
                .build();

        //when
        final var notesCommand = converter.convert(notes);

        //then
        assertNotNull(notesCommand);
        assertEquals(ID_VALUE, notesCommand.getId());
        assertEquals(RECIPE_NOTES, notesCommand.getRecipeNotes());
    }

    @Test
    void testNull() {
        assertNull(converter.convert(null));
    }

    @Test
    void testEmptyObject() {
        assertNotNull(converter.convert(new Notes()));
    }
}
