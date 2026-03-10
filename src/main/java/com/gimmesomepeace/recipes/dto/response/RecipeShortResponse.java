package com.gimmesomepeace.recipes.dto.response;


import com.gimmesomepeace.recipes.model.Recipe;

public record RecipeShortResponse(
        Long id,
        String title,
        UserShortResponse user,
        CategoryShortResponse category
) {
    public static RecipeShortResponse from(Recipe recipe) {
        return new RecipeShortResponse(
                recipe.getId(),
                recipe.getTitle(),
                UserShortResponse.from(recipe.getUser()),
                CategoryShortResponse.from(recipe.getCategory())
        );
    }
}
