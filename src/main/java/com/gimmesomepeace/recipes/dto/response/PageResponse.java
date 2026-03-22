package com.gimmesomepeace.recipes.dto.response;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;


@Schema(description = "Стандартная пагинация для всех DTO")
public record PageResponse<T>(
        @ArraySchema(schema = @Schema(description = "Список элементов страницы"))
        List<T> content,

        @Schema(description = "номер текущей страницы (начиная с 0)")
        int page,

        @Schema(description = "Размер страницы")
        int size,

        @Schema(description = "Общее кол-во элементов")
        Long totalElements,

        @Schema(description = "Общее кол-во страниц")
        int totalPages,

        @Schema(description = "Является ли страница первой")
        boolean first,

        @Schema(description = "Является ли страница последней")
        boolean last
) {
    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }
}
