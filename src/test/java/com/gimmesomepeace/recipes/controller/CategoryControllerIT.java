package com.gimmesomepeace.recipes.controller;

import com.gimmesomepeace.recipes.model.Category;
import com.gimmesomepeace.recipes.model.User;
import com.gimmesomepeace.recipes.repository.CategoryRepository;
import com.gimmesomepeace.recipes.repository.UserRepository;
import com.gimmesomepeace.recipes.security.JwtUtil;
import com.gimmesomepeace.recipes.testutil.TestDatabaseCleaner;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
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
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    TestDatabaseCleaner cleaner;

    private String token;

    @BeforeEach
    void setUp() {
        cleaner.clean();
        User user = User.builder()
                        .name("name")
                        .login("login")
                        .passwordHash("password")
                        .build();
        user = userRepository.save(user);

        token = jwtUtil.generateToken(user);
    }

    @Test
    void getCategories_shouldFailWhenNotAuthorized() throws Exception {
        mockMvc.perform(get("/categories"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getById_shouldFailWhenNotAuthorized() throws Exception {
        mockMvc.perform(get("/categories/1"))
                .andExpect(status().isForbidden());
    }


    @Test
    void getCategories_shouldReturnAllCategories() throws Exception {
        Category category = Category.builder().title("Тестовая категория").build();
        repository.save(category);

        mockMvc.perform(get("/categories")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].title", hasItem("Тестовая категория")));
    }

    @Test
    void getById_shouldReturnCategory() throws Exception {
        Category category = Category.builder().title("Тестовая категория").build();
        category = repository.save(category);

        mockMvc.perform(get("/categories/{id}", category.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(category.getId()))
                .andExpect(jsonPath("$.title").value(category.getTitle()));
    }

    @Test
    void getById_shouldReturn404WhenNotFound() throws Exception {
        mockMvc.perform(get("/categories/{id}", -1)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").isNotEmpty())
                .andExpect(jsonPath("$.status").value(404));
    }
}
