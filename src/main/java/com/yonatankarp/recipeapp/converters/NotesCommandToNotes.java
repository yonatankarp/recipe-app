package com.yonatankarp.recipeapp.converters;

import com.yonatankarp.recipeapp.commands.NotesCommand;
import com.yonatankarp.recipeapp.model.Notes;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("ConstantConditions")
public class NotesCommandToNotes implements Converter<NotesCommand, Notes> {

    @Synchronized
    @Nullable
    @Override
    public Notes convert(final NotesCommand source) {
        if(source == null) {
            return null;
        }
        return Notes.builder()
                .id(source.getId())
                .recipeNotes(source.getRecipeNotes())
                .build();
    }
}
