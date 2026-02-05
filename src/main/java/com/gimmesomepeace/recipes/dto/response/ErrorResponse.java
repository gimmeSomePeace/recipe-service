package com.gimmesomepeace.recipes.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Ошибка API")
public record ErrorResponse(
        @Schema(description = "Сообщение об ошибке", example = "Рецепт не найден") String message
) {}
