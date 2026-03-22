package com.gimmesomepeace.recipes.dto.response;

import com.gimmesomepeace.recipes.model.Recipe;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;


@Schema(description = "Данные рецепта")
public record RecipeResponse(
        @Schema(description = "Уникальный идентификатор рецепта", example = "1")
        Long id,

        @Schema(description = "Заголовок рецепта", example = "Десерт")
        String title,

        @Schema(description = "Пошаговая инструкция по приготовлению блюда")
        String instructions,

        @Schema(description = "Оценка блюда")
        Integer rating,

        @Schema(description = "Заметки о блюде")
        String notes,

        @Schema(description = "Дата создания блюда", example = "2026-03-22T12:34:56Z")
        Instant createdAt,

        @Schema(description = "Дата последнего обновления информации о блюде", example = "2026-03-22T12:34:56Z")
        Instant updatedAt,

        @Schema(description = "Информация о создателе рецепта")
        UserShortResponse user,

        @Schema(description = "Информация о категории, к которой относится рецепт")
        CategoryShortResponse category
) {
    public static RecipeResponse from(Recipe recipe) {
        return new RecipeResponse(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getInstructions(),
                recipe.getRating(),
                recipe.getNotes(),
                recipe.getCreatedAt(),
                recipe.getUpdatedAt(),
                UserShortResponse.from(recipe.getUser()),
                CategoryShortResponse.from(recipe.getCategory())
        );
    }
}
