package com.yonatankarp.recipeapp.converters;

import com.yonatankarp.recipeapp.commands.CategoryCommand;
import com.yonatankarp.recipeapp.model.Category;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("ConstantConditions")
public class CategoryToCategoryCommand implements Converter<Category, CategoryCommand> {

    @Synchronized
    @Nullable
    @Override
    public CategoryCommand convert(final Category source) {
        if(source == null) {
            return null;
        }
        return CategoryCommand.builder()
                .id(source.getId())
                .description(source.getDescription())
                .build();
    }
}
