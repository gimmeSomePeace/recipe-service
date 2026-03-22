package com.gimmesomepeace.recipes.controller;

import com.gimmesomepeace.recipes.dto.request.CreateRecipeRequest;
import com.gimmesomepeace.recipes.dto.request.UpdateRecipeRequest;
import com.gimmesomepeace.recipes.dto.response.ErrorResponse;
import com.gimmesomepeace.recipes.dto.response.PageResponse;
import com.gimmesomepeace.recipes.dto.response.RecipeResponse;
import com.gimmesomepeace.recipes.dto.response.RecipeShortResponse;
import com.gimmesomepeace.recipes.security.UserPrincipal;
import com.gimmesomepeace.recipes.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;


@Tag(name = "Рецепты", description = "Операции, связанные с рецептами")
@RestController
@RequestMapping(value = "/recipes", produces = MediaType.APPLICATION_JSON_VALUE)
public class RecipeController {

    private final RecipeService recipeService;
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Operation(
            summary = "Получение рецептов через пагинацию",
            description = "Возвращает набор рецептов от всех пользователей, используя стандартные правила пагинации"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Рецепты получены",
                    content = @Content(
                            schema = @Schema(implementation = PageResponse.class)
                    )
            )
    })
    @GetMapping
    public PageResponse<RecipeShortResponse> getRecipes(
            @ParameterObject
            @PageableDefault(sort = "title", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return recipeService.getRecipes(pageable);
    }

    @Operation(summary = "Подробные данные об одном рецепте", description = "Возвращает все данные об одном рецепте")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Информация о рецепте получена",
                    content = @Content(
                            schema = @Schema(implementation = RecipeResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Рецепт не найден",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public RecipeResponse getRecipe(@PathVariable Long id) {
        return recipeService.getRecipe(id);
    }

    @Operation(summary = "Обновляет данные о рецепте")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Данные о рецепте успешно обновлены",
                    content = @Content(
                            schema = @Schema(implementation = RecipeResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Данные не прошли валидацию",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Отказано в доступе",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Рецепт не найден",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PatchMapping("/{id}")
    public RecipeResponse updateRecipe(
            @AuthenticationPrincipal UserPrincipal user,
            @PathVariable Long id,
            @Valid @RequestBody UpdateRecipeRequest updateRecipeRequest
    ) {
        return recipeService.updateRecipe(user.getId(), id, updateRecipeRequest);
    }

    @Operation(summary = "Удаление рецепта", description = "Удаляет рецепт по id")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Рецепт успешно удален",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Отказано в доступе",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Рецепт не найден",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(
            @AuthenticationPrincipal UserPrincipal user,
            @PathVariable Long id
    ) {
        recipeService.delete(user.getId(), id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Создание нового рецепта",
            description = "Создает новый рецепт на основе входных данных"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Рецепт успешно создан",
                    content = @Content(
                            schema = @Schema(implementation = RecipeResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Данные не прошли валидацию",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Отказано в доступе",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь или категория не найдены",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<RecipeResponse> createRecipe(
            @AuthenticationPrincipal UserPrincipal user,
            @Valid @RequestBody CreateRecipeRequest createRecipeRequest
    ) {
        RecipeResponse recipe = recipeService.create(user.getId(), createRecipeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(recipe);
    }
}
