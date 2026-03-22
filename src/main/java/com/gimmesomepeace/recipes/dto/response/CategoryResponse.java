package com.gimmesomepeace.recipes.dto.response;

import com.gimmesomepeace.recipes.model.Category;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;


@Schema(description = "Категории рецептов")
public record CategoryResponse(
        @Schema(
                description = "Уникальный идентификатор категории",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "1"
        ) Long id,

        @Schema(
                description = "Название категории",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "Десерт"
        ) String title,

        @Schema(description = "Дата создания категории", example = "2026-03-22T12:34:56Z")
        Instant createdAt,

        @Schema(description = "Дата последнего обновления категории", example = "2026-03-22T12:34:56Z")
        Instant updatedAt
) {
        public static CategoryResponse from(Category c) {
                return new CategoryResponse(
                        c.getId(),
                        c.getTitle(),
                        c.getCreatedAt(),
                        c.getUpdatedAt()
                );
        }
}
