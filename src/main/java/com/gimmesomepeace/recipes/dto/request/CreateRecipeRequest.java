package com.gimmesomepeace.recipes.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


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
        @NotBlank(message = "Поле 'category_id' не может быть пустым")
        Long categoryId,

        @Schema(description = "Оценка блюда")
        @Size(min = 0, max = 10, message = "Поле 'rating' должно находиться в пределах от 0 до 10")
        Integer rating,

        @Schema(description = "Заметки о блюде")
        String notes
) { }
