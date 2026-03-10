package com.gimmesomepeace.recipes.dto.request;

public record CreateRecipeRequest(
        String title,
        String instructions,
        Long categoryId,
        Integer rating,
        String notes
) { }
