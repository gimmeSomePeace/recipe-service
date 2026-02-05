package com.gimmesomepeace.recipes.service;


import com.gimmesomepeace.recipes.dto.CategoryDto;
import com.gimmesomepeace.recipes.mapper.CategoryMapper;
import com.gimmesomepeace.recipes.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public List<CategoryDto> getAll() {
        return repository.findAll()
                .stream()
                .map(CategoryMapper::toDto)
                .toList();
    }
}
