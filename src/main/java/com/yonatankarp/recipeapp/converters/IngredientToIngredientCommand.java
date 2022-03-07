package com.yonatankarp.recipeapp.converters;

import com.yonatankarp.recipeapp.commands.IngredientCommand;
import com.yonatankarp.recipeapp.model.Ingredient;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@SuppressWarnings("ConstantConditions")
public class IngredientToIngredientCommand implements Converter<Ingredient, IngredientCommand> {

    private final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;

    @Synchronized
    @Nullable
    @Override
    public IngredientCommand convert(final Ingredient source) {
        if(source == null) {
            return null;
        }

        return IngredientCommand.builder()
                .id(source.getId())
                .description(source.getDescription())
                .amount(source.getAmount())
                .uom(unitOfMeasureToUnitOfMeasureCommand.convert(source.getUom()))
                .build();
    }
}
