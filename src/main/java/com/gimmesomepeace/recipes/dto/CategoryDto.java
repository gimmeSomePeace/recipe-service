package com.gimmesomepeace.recipes.dto;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(name = "CategoryDto", description = "Категории рецептов")
public record CategoryDto(
        @Schema(
                description = "Уникальный идентификатор категории",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "1"
        ) Long id,
        @Schema(
                description = "Название категории",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "Десерт"
        ) String title
) {}
