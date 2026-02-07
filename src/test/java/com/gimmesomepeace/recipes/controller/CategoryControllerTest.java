package com.gimmesomepeace.recipes.controller;

import com.gimmesomepeace.recipes.dto.CategoryDto;
import com.gimmesomepeace.recipes.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

    @Mock
    private CategoryService service;

    @InjectMocks
    private CategoryController controller;

    @Test
    void getCategories_shouldReturnAllCategories() {
        List<CategoryDto> categories = List.of(
                new CategoryDto(1L, "Категория 1"),
                new CategoryDto(2L, "Категория 2")
        );
        when(service.getAll()).thenReturn(categories);

        List<CategoryDto> result = controller.getCategories();

        assertEquals(result, categories);
    }

    @Test
    void getById_shouldReturnCategoryById() {
        Long categoryId = 1L;
        CategoryDto category = new CategoryDto(categoryId, "Категория " + categoryId);
        when(service.getById(categoryId)).thenReturn(category);

        CategoryDto result = controller.getById(categoryId);

        assertEquals(result, category);
    }
}
