package com.yonatankarp.recipeapp.converters;

import com.yonatankarp.recipeapp.commands.CategoryCommand;
import com.yonatankarp.recipeapp.model.Category;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("ConstantConditions")
public class CategoryCommandToCategory implements Converter<CategoryCommand, Category>{

    @Synchronized
    @Nullable
    @Override
    public Category convert(final CategoryCommand source) {
        if (source == null) {
            return null;
        }

        return Category.builder()
                .id(source.getId())
                .description(source.getDescription())
                .build();
    }
}
