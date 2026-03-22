package com.gimmesomepeace.recipes.service;


import com.gimmesomepeace.recipes.dto.response.CategoryResponse;
import com.gimmesomepeace.recipes.dto.response.CategoryShortResponse;
import com.gimmesomepeace.recipes.dto.response.PageResponse;
import com.gimmesomepeace.recipes.exception.ResourceNotFoundException;
import com.gimmesomepeace.recipes.exception.ResourceType;
import com.gimmesomepeace.recipes.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public PageResponse<CategoryShortResponse> getAll(Pageable pageable) {
        Page<CategoryShortResponse> categories = repository.findAll(pageable).map(CategoryShortResponse::from);
        return PageResponse.from(categories);
    }

    public CategoryResponse getById(Long id) {
        return repository.findById(id)
                .map(CategoryResponse::from)
                .orElseThrow(() ->
                        new ResourceNotFoundException(ResourceType.CATEGORY, id)
                );
    }
}
