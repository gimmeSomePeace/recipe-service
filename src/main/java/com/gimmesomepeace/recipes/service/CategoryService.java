package com.gimmesomepeace.recipes.service;


import com.gimmesomepeace.recipes.dto.CategoryDto;
import com.gimmesomepeace.recipes.mapper.CategoryMapper;
import com.gimmesomepeace.recipes.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public CategoryDto getById(Long id) {
        return repository.findById(id)
                .map(CategoryMapper::toDto)
                .orElseThrow(() ->
                        new EntityNotFoundException("Категория с идентификатором " + id + " не найдена")
                );
    }
}
