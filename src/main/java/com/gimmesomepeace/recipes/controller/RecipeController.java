package com.gimmesomepeace.recipes.controller;

import com.gimmesomepeace.recipes.dto.request.CreateRecipeRequest;
import com.gimmesomepeace.recipes.dto.request.UpdateRecipeRequest;
import com.gimmesomepeace.recipes.dto.response.RecipeResponse;
import com.gimmesomepeace.recipes.dto.response.RecipeShortResponse;
import com.gimmesomepeace.recipes.dto.response.UserShortResponse;
import com.gimmesomepeace.recipes.security.UserPrincipal;
import com.gimmesomepeace.recipes.service.RecipeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;


@RestController
@RequestMapping(value = "/recipes", produces = MediaType.APPLICATION_JSON_VALUE)
public class RecipeController {

    private final RecipeService recipeService;
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public Page<RecipeShortResponse> getRecipes(
            @PageableDefault(size = 10, sort = "title", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return recipeService.getRecipes(pageable);
    }

    @GetMapping("/{id}")
    public RecipeResponse getRecipe(@PathVariable Long id) {
        return recipeService.getRecipe(id);
    }

    @PatchMapping("/{id}")
    public RecipeResponse updateRecipe(
            @AuthenticationPrincipal UserPrincipal user,
            @PathVariable Long id,
            @RequestBody UpdateRecipeRequest updateRecipeRequest
    ) {
        return recipeService.updateRecipe(user.getId(), id, updateRecipeRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(
            @AuthenticationPrincipal UserPrincipal user,
            @PathVariable Long id
    ) {
        recipeService.delete(user.getId(), id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<RecipeResponse> createRecipe(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody CreateRecipeRequest createRecipeRequest
    ) {
        RecipeResponse recipe = recipeService.create(user.getId(), createRecipeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(recipe);
    }
}
