package com.gimmesomepeace.recipes.service;

import com.gimmesomepeace.recipes.dto.response.RecipeResponse;
import com.gimmesomepeace.recipes.dto.response.RecipeShortResponse;
import com.gimmesomepeace.recipes.dto.response.UserShortResponse;
import com.gimmesomepeace.recipes.exception.ResourceNotFoundException;
import com.gimmesomepeace.recipes.exception.ResourceType;
import com.gimmesomepeace.recipes.model.Recipe;
import com.gimmesomepeace.recipes.repository.RecipeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Page<RecipeShortResponse> getRecipesByUserId(
            Long userId,
            Pageable pageable
    ) {
        return recipeRepository.findByUserId(userId, pageable).map(RecipeShortResponse::from);
    }

    public Page<RecipeShortResponse> getRecipes(
            Pageable pageable
    ) {
        return recipeRepository.findAll(pageable).map(RecipeShortResponse::from);
    }

    public RecipeResponse getRecipe(Long id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ResourceType.RECIPE, id));
        return RecipeResponse.from(recipe);
    }
}
