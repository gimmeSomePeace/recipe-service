package com.gimmesomepeace.recipes.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;


@Schema(description = "Данные для создания рецепта")
public record CreateRecipeRequest(
        @Schema(description = "Заголовок рецепта")
        @Size(max = 255, message = "Заголовок не может быть длиннее 255 символов")
        @NotBlank(message = "Поле 'title' не может быть пустым")
        String title,

        @Schema(description = "Пошаговая инструкция по приготовлению блюда")
        @NotBlank(message = "Поле 'instructions' не может быть пустым")
        String instructions,

        @Schema(description = "id категории, которой принадлежит рецепт")
        @NotNull(message = "Поле 'category_id' не может быть пустым")
        Long categoryId,

        @Schema(description = "Оценка блюда")
        @Min(value = 0, message = "Поле 'rating' не может быть меньше 0")
        @Max(value = 10, message = "Поле 'rating' не может быть больше 10")
        Integer rating,

        @Schema(description = "Заметки о блюде")
        @Size(max = 1000, message = "Поле 'notes' не должно превышать 1000 символов")
        String notes
) { }
