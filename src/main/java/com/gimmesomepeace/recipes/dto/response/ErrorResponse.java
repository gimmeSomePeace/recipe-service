package com.gimmesomepeace.recipes.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;


@Schema(description = "Ошибка API, RFC 7807")
public record ErrorResponse(
        @Schema(description = "Тип ошибки", example = "https://api.example.com/problems/recipe-not-found") String type,
        @Schema(description = "Сообщение об ошибке", example = "Рецепт не найден") String title,
        @Schema(description = "Статус ошибки", example = "404") int status,
        @Schema(description = "Человекочитаемое сообщение", example = "Рецепт с идентификатором 5 не найден") String detail,
        @Schema(description = "url запроса", example = "/recipes/5") String instance,
        @Schema(description = "Временная метка запроса", example = "2026-02-07T12:45:30Z") Instant timestamp
) {}
