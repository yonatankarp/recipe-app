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
public class IngredientCommandToIngredient implements Converter<IngredientCommand, Ingredient> {

    private final UnitOfMeasureCommandToUnitOfMeasure unitOfMeasureCommandToUnitOfMeasure;

    @Synchronized
    @Nullable
    @Override
    public Ingredient convert(final IngredientCommand source) {
        if(source == null) {
            return null;
        }

        return Ingredient.builder()
                .id(source.getId())
                .description(source.getDescription())
                .amount(source.getAmount())
                .uom(unitOfMeasureCommandToUnitOfMeasure.convert(source.getUom()))
                .build();
    }
}
