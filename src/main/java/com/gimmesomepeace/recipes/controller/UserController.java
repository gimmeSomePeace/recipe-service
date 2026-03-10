package com.gimmesomepeace.recipes.controller;

import com.gimmesomepeace.recipes.dto.request.UpdateUserRequest;
import com.gimmesomepeace.recipes.dto.response.ErrorResponse;
import com.gimmesomepeace.recipes.dto.response.RecipeShortResponse;
import com.gimmesomepeace.recipes.dto.response.UserResponse;
import com.gimmesomepeace.recipes.security.UserPrincipal;
import com.gimmesomepeace.recipes.service.RecipeService;
import com.gimmesomepeace.recipes.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Пользователи", description = "Операции, связанные с пользователями")
@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService service;
    private final RecipeService recipeService;

    public UserController(UserService service, RecipeService recipeService) {
        this.service = service;
        this.recipeService = recipeService;
    }

    @Operation(
            summary = "Информация о пользователе",
            description = "Возвращает информацию об пользователе, выполнившем аутентификацию"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Информация о пользователе получена",
                    content = @Content(
                            schema = @Schema(implementation = UserResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Требуется аутентификация",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
    })
    @GetMapping("/me")
    UserResponse getUserInfo(@AuthenticationPrincipal UserPrincipal principal) {
        return service.getUserInfo(principal.getId());
    }

    @Operation(summary = "Обновить информацию о пользователе")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Информация успешно обновлена",
                    content = @Content(
                            schema = @Schema(implementation = UserResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Требуется аутентификация",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Логин уже занят другим пользователем",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PatchMapping("/me")
    UserResponse updateUserInfo(
            @RequestBody UpdateUserRequest request,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return service.updateUser(principal.getId(), request);
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Пользователь успешно удален",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Требуется аутентификация",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
    })
    @DeleteMapping("/me")
    ResponseEntity<Void> deleteUser(@AuthenticationPrincipal UserPrincipal principal) {
        service.deleteUser(principal.getId());
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Рецепты авторизованного пользователя",
            description = "Возвращает рецепты авторизованного пользователя через пагинацию"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Рецепты получены",
                    content = @Content(
                            // TODO: Поменять на кастомный Page<RecipeShortResponse>
                            schema = @Schema(implementation = RecipeShortResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Необходима авторизация",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/me/recipes")
    Page<RecipeShortResponse> getUserRecipes(
            @AuthenticationPrincipal UserPrincipal principal,
            @PageableDefault(size = 10, sort = "title", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return recipeService.getRecipesByUserId(
                principal.getId(),
                pageable
        );
    }

    @Operation(
            summary = "Рецепты пользователя",
            description = "Возвращает рецепты некоторого пользователя через пагинацию"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Получены рецепты некоторого пользователя",
                    content = @Content(
                            // TODO: знаю
                            schema = @Schema(implementation = RecipeShortResponse.class)
                    )
            )
    })
    @GetMapping("/{id}/recipes")
    Page<RecipeShortResponse> getRecipesByUserId(
            @PathVariable long id,
            @PageableDefault(size = 10, sort = "title", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return recipeService.getRecipesByUserId(
                id,
                pageable
        );
    }
}
