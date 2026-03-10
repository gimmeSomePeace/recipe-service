package com.gimmesomepeace.recipes.dto.response;

import com.gimmesomepeace.recipes.model.Recipe;

public record RecipeResponse(
        Long id,
        String title,
        String instruction,
        Integer rating,
        String notes,
        UserShortResponse user,
        CategoryShortResponse category
) {
    public static RecipeResponse from(Recipe recipe) {
        return new RecipeResponse(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getInstructions(),
                recipe.getRating(),
                recipe.getNotes(),
                UserShortResponse.from(recipe.getUser()),
                CategoryShortResponse.from(recipe.getCategory())
        );
    }
}
