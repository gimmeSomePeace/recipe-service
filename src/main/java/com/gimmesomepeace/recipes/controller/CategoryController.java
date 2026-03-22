package com.gimmesomepeace.recipes.controller;


import com.gimmesomepeace.recipes.dto.response.CategoryResponse;
import com.gimmesomepeace.recipes.dto.response.ErrorResponse;
import com.gimmesomepeace.recipes.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Tag(name = "Категории", description = "Операции, связанные с категориями")
@RestController
@RequestMapping(value = "/categories", produces =  MediaType.APPLICATION_JSON_VALUE)
public class CategoryController {
    CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @Operation(summary = "Получение всех категорий", description = "Возвращает все категории")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Категории получены",
                content = @Content(
                        array = @ArraySchema(schema = @Schema(implementation = CategoryResponse.class))
                )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Требуется аутентификация",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping
    public List<CategoryResponse> getCategories() {
        return service.getAll();
    }

    @Operation(
            summary = "Получение категории по id",
            description = "Возвращает категорию, имеющую соответствующий id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Категория найдена",
                    content = @Content(
                            schema = @Schema(implementation = CategoryResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Категория не найдена",
                    content = @Content(
                        schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Ошибка 404",
                                    value = """
                                           {
                                              "message": "Категория с идентификатором 5 не найдена",
                                              "status" : 404
                                           }
                                           """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Требуется аутентификация",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                    )
            )
    })
    @GetMapping("/{id}")
    public CategoryResponse getById(
            @Parameter(description = "Id категории", example = "5")
            @PathVariable Long id) {
        return service.getById(id);
    }
}
