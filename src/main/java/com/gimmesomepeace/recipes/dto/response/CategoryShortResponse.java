package com.gimmesomepeace.recipes.dto.response;

import com.gimmesomepeace.recipes.model.Category;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;


@Schema(description = "Основные данные категории, используется для пагинации")
public record CategoryShortResponse(
        @Schema(description = "Уникальный идентификатор категории", example = "1")
        Long id,

        @Schema(description = "Заголовок категории", example = "Десерт")
        String title,

        @Schema(description = "Дата создания категории", example = "2026-03-22T12:34:56Z")
        Instant createdAt,

        @Schema(description = "Дата последнего обновления категории", example = "2026-03-22T12:34:56Z")
        Instant updatedAt
) {
    public static CategoryShortResponse from(Category category) {
        return new CategoryShortResponse(
                category.getId(),
                category.getTitle(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}
