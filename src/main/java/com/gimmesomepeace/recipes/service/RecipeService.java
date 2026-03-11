package com.gimmesomepeace.recipes.service;

import com.gimmesomepeace.recipes.dto.request.CreateRecipeRequest;
import com.gimmesomepeace.recipes.dto.request.UpdateRecipeRequest;
import com.gimmesomepeace.recipes.dto.response.RecipeResponse;
import com.gimmesomepeace.recipes.dto.response.RecipeShortResponse;
import com.gimmesomepeace.recipes.exception.ForbiddenException;
import com.gimmesomepeace.recipes.exception.RecipeValidationException;
import com.gimmesomepeace.recipes.exception.ResourceNotFoundException;
import com.gimmesomepeace.recipes.exception.ResourceType;
import com.gimmesomepeace.recipes.model.Category;
import com.gimmesomepeace.recipes.model.Recipe;
import com.gimmesomepeace.recipes.model.User;
import com.gimmesomepeace.recipes.repository.CategoryRepository;
import com.gimmesomepeace.recipes.repository.RecipeRepository;
import com.gimmesomepeace.recipes.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public RecipeService(RecipeRepository recipeRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public Page<RecipeShortResponse> getRecipesByUserId(
            Long userId,
            Pageable pageable
    ) {
        userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(ResourceType.USER, userId));
        return recipeRepository.findByUserId(userId, pageable).map(RecipeShortResponse::from);
    }

    public Page<RecipeShortResponse> getRecipes(Pageable pageable) {
        return recipeRepository.findAll(pageable).map(RecipeShortResponse::from);
    }

    public RecipeResponse getRecipe(Long id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ResourceType.RECIPE, id));
        return RecipeResponse.from(recipe);
    }

    public RecipeResponse updateRecipe(Long userId, Long recipeId, UpdateRecipeRequest request) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new ResourceNotFoundException(ResourceType.RECIPE, recipeId));
        if (!userId.equals(recipe.getUser().getId()) ) throw new ForbiddenException("Редактирование рецепта доступно исключительно владельцу");

        if (request.title() != null) {
            if (request.title().isBlank())
                throw new RecipeValidationException("Поле title не может быть пустым");
            recipe.setTitle(request.title());
        }

        if (request.instructions() != null) {
            if (request.instructions().isBlank())
                throw new RecipeValidationException("Поле instructions не может быть пустым");
            recipe.setInstructions(request.instructions());
        }

        if (request.categoryId() != null) {
            Category category = categoryRepository.findById(request.categoryId()).orElseThrow(() -> new ResourceNotFoundException(ResourceType.CATEGORY, request.categoryId()));
            recipe.setCategory(category);
        }

        if (request.rating() != null) recipe.setRating(request.rating());
        if (request.notes() != null) recipe.setNotes(request.notes());

        recipeRepository.save(recipe);
        return RecipeResponse.from(recipe);
    }

    public RecipeResponse create(Long userId, CreateRecipeRequest request) {
        if (request.title() == null || request.title().isBlank()) throw new RecipeValidationException("Поле title не может быть пустым");
        if (request.instructions() == null || request.instructions().isBlank()) throw new RecipeValidationException("Поле instructions не может быть пустым");
        if (request.categoryId() == null) throw new RecipeValidationException("Поле category_id не может быть пустым");

        Category category = categoryRepository.findById(request.categoryId()).orElseThrow(() -> new ResourceNotFoundException(ResourceType.CATEGORY, request.categoryId()));
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(ResourceType.USER, userId));

        Recipe recipe = Recipe.builder()
                .title(request.title())
                .instructions(request.instructions())
                .category(category)
                .user(user)
                .rating(request.rating())
                .notes(request.notes())
                .build();
        recipeRepository.save(recipe);
        return RecipeResponse.from(recipe);
    }

    public void delete(Long userId, Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new ResourceNotFoundException(ResourceType.RECIPE, recipeId));
        if (!recipe.getUser().getId().equals(userId)) throw new ForbiddenException("Удалить рецепт может только его владелец");
        recipeRepository.delete(recipe);
    }
}

