package com.gimmesomepeace.recipes.dto.request;

public record UpdateRecipeRequest(
        String title,
        String instructions,
        Long categoryId,
        Integer rating,
        String notes
) { }
