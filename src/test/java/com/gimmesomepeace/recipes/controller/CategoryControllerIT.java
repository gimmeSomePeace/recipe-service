package com.gimmesomepeace.recipes.controller;

import com.gimmesomepeace.recipes.model.Category;
import com.gimmesomepeace.recipes.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.hasItem;


@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class CategoryControllerIT {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    CategoryRepository repository;


    @Test
    void getCategories_shouldReturnAllCategories() throws Exception {
        repository.save(new Category("Тестовая категория"));

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath(("$[*].title")).value(hasItem("Тестовая категория")));
    }

    @Test
    void getById_shouldReturnCategory() throws Exception {
        Category category = repository.save(new Category("Еда"));

        mockMvc.perform(get("/categories/{id}", category.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(category.getId()))
                .andExpect(jsonPath("$.title").value(category.getTitle()));
    }

    @Test
    void getById_shouldReturn404WhenNotFound() throws Exception {
        mockMvc.perform(get("/categories/{id}", -1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").isNotEmpty())
                .andExpect(jsonPath("$.status").value(404));
    }
}
