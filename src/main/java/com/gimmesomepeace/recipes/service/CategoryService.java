package com.gimmesomepeace.recipes.service;


import com.gimmesomepeace.recipes.dto.response.CategoryResponse;
import com.gimmesomepeace.recipes.exception.ResourceNotFoundException;
import com.gimmesomepeace.recipes.exception.ResourceType;
import com.gimmesomepeace.recipes.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CategoryService {
    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public List<CategoryResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(CategoryResponse::from)
                .toList();
    }

    public CategoryResponse getById(Long id) {
        return repository.findById(id)
                .map(CategoryResponse::from)
                .orElseThrow(() ->
                        new ResourceNotFoundException(ResourceType.CATEGORY, id)
                );
    }
}
