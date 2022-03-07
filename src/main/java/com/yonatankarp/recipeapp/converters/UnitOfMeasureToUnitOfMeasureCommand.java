package com.yonatankarp.recipeapp.converters;

import com.yonatankarp.recipeapp.commands.UnitOfMeasureCommand;
import com.yonatankarp.recipeapp.model.UnitOfMeasure;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("ConstantConditions")
public class UnitOfMeasureToUnitOfMeasureCommand implements Converter<UnitOfMeasure, UnitOfMeasureCommand> {

    @Synchronized
    @Nullable
    @Override
    public UnitOfMeasureCommand convert(final UnitOfMeasure source) {
        if(source == null) {
            return null;
        }
        return UnitOfMeasureCommand.builder()
                .id(source.getId())
                .description(source.getDescription())
                .build();
    }
}
