package com.gimmesomepeace.recipes.controller;


import com.gimmesomepeace.recipes.dto.CategoryDto;
import com.gimmesomepeace.recipes.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class CategoryController {
    CategoryService service;

    @GetMapping("/categories")
    public List<CategoryDto> getCategories() {
        return service.getAll();
    }
}
