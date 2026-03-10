package com.gimmesomepeace.recipes.dto.response;

import com.gimmesomepeace.recipes.model.Category;


public record CategoryShortResponse(Long id, String title) {
    static CategoryShortResponse from(Category category) {
        return new CategoryShortResponse(
                category.getId(),
                category.getTitle()
        );
    }
}
