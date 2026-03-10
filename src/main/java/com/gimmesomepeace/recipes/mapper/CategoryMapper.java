package com.gimmesomepeace.recipes.mapper;

import com.gimmesomepeace.recipes.dto.CategoryDto;
import com.gimmesomepeace.recipes.model.Category;

public class CategoryMapper {

    public static CategoryDto toDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getTitle()
        );
    }
}
