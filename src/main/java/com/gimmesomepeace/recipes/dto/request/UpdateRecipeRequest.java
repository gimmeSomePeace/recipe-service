package com.gimmesomepeace.recipes.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;


@Schema(description = "Данные для обновления данных о рецепте")
public record UpdateRecipeRequest(
        @Schema(description = "Заголовок рецепта ")
        @Size(min = 1, max = 255, message = "Заголовок не может быть длиннее 255 символов")
        String title,

        @Schema(description = "Пошаговая инструкция по приготовлению блюда")
        @Size(min = 1, message = "Поле 'instructions' должно быть либо null, либо длиной как минимум 1 символ")
        String instructions,

        @Schema(description = "id категории, которой принадлежит рецепт")
        @Positive(message = "Поле 'category_id' должен быть положительным")
        Long categoryId,

        @Schema(description = "Оценка блюда")
        @Min(value = 0, message = "Поле 'rating' не может быть меньше 0")
        @Max(value = 10, message = "Поле 'rating' не может быть больше 10")
        Integer rating,

        @Schema(description = "Заметки о блюде")
        @Size(max = 1000, message = "Поле 'notes' не должно превышать 1000 символов")
        String notes
) { }
