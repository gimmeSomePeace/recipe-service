package com.gimmesomepeace.recipes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gimmesomepeace.recipes.dto.request.CreateRecipeRequest;
import com.gimmesomepeace.recipes.dto.request.UpdateRecipeRequest;
import com.gimmesomepeace.recipes.model.Category;
import com.gimmesomepeace.recipes.model.Recipe;
import com.gimmesomepeace.recipes.model.User;
import com.gimmesomepeace.recipes.repository.CategoryRepository;
import com.gimmesomepeace.recipes.repository.RecipeRepository;
import com.gimmesomepeace.recipes.repository.UserRepository;
import com.gimmesomepeace.recipes.security.JwtUtil;
import com.gimmesomepeace.recipes.testutil.TestDatabaseCleaner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class RecipeControllerIT {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    TestDatabaseCleaner cleaner;

    User testUser;
    Category testCategory;

    @BeforeEach
    void setUp() {
        cleaner.clean();

        recipeRepository.deleteAll();
        userRepository.deleteAll();
        categoryRepository.deleteAll();

        testCategory = Category.builder().title("Test Category").build();
        testCategory = categoryRepository.save(testCategory);

        testUser = User.builder()
                        .name("test")
                        .login("test")
                        .passwordHash("password-hash")
                        .build();
        testUser = userRepository.save(testUser);
    }

    @Test
    void getRecipes_shouldReturnAllRecipes() throws Exception {
        Recipe recipe1 = Recipe.builder()
                            .title("recipe-test-1")
                            .instructions("recipe-instructions-1")
                            .category(testCategory)
                            .user(testUser)
                            .build();
        Recipe recipe2 = Recipe.builder()
                            .title("recipe-test-2")
                            .instructions("recipe-instructions-2")
                            .category(testCategory)
                            .user(testUser)
                            .build();
        recipe1 = recipeRepository.save(recipe1);
        recipe2 = recipeRepository.save(recipe2);

        mockMvc.perform(get("/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[*].title",
                        containsInAnyOrder(recipe1.getTitle(), recipe2.getTitle())));
    }

    @Test
    void getRecipe_shouldReturnRecipe() throws Exception {
        Recipe recipe1 = Recipe.builder()
                .title("recipe-test-1")
                .instructions("recipe-instructions-1")
                .category(testCategory)
                .user(testUser)
                .build();
        recipe1 = recipeRepository.save(recipe1);

        mockMvc.perform(get("/recipes/" + recipe1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(recipe1.getId()));
    }

    @Test
    void getRecipe_shouldFailWhenIdIsIncorrect() throws Exception {
        mockMvc.perform(get("/recipes/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateRecipe_shouldFailWhenNotAuthorized() throws Exception {
        mockMvc.perform(patch("/recipes/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateRecipe_shouldUpdateRecipe() throws Exception {
        Recipe recipe = Recipe.builder()
                .title("recipe-test")
                .instructions("recipe-instructions")
                .category(testCategory)
                .user(testUser)
                .build();
        recipe = recipeRepository.save(recipe);

        UpdateRecipeRequest updateRecipeRequest = new UpdateRecipeRequest("new-title", null, null, null, null);

        String token = jwtUtil.generateToken(testUser);
        mockMvc.perform(patch("/recipes/" + recipe.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRecipeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(recipe.getId()))
                .andExpect(jsonPath("$.title").value(updateRecipeRequest.title()));

        Recipe updatedRecipe = recipeRepository.findById(recipe.getId()).orElse(null);
        assertNotNull(updatedRecipe);
        assertEquals(updatedRecipe.getTitle(), updateRecipeRequest.title());
    }

    @Test
    void updateRecipe_shouldFailWhenUserIsNotOwner() throws Exception {
        User anotherUser = User.builder()
                            .name("another")
                            .login("another")
                            .passwordHash("password-hash")
                            .build();
        anotherUser = userRepository.save(anotherUser);

        Recipe recipe = Recipe.builder()
                .title("recipe-test")
                .instructions("recipe-instructions")
                .category(testCategory)
                .user(testUser)
                .build();
        recipe = recipeRepository.save(recipe);

        UpdateRecipeRequest updateRecipeRequest = new UpdateRecipeRequest("new-title", null, null, null, null);

        String token = jwtUtil.generateToken(anotherUser);
        String content = objectMapper.writeValueAsString(updateRecipeRequest);
        mockMvc.perform(patch("/recipes/" + recipe.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateRecipe_shouldFailWhenCategoryDoesNotExist() throws Exception {
        Recipe recipe = Recipe.builder()
                .title("recipe-test")
                .instructions("recipe-instructions")
                .category(testCategory)
                .user(testUser)
                .build();
        recipe = recipeRepository.save(recipe);

        UpdateRecipeRequest updateRecipeRequest = new UpdateRecipeRequest(null, null, -1L, null, null);

        String token = jwtUtil.generateToken(testUser);
        String content = objectMapper.writeValueAsString(updateRecipeRequest);
        mockMvc.perform(patch("/recipes/" + recipe.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteRecipe_shouldFailWhenNotAuthorized() throws Exception {
        mockMvc.perform(delete("/recipes/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteRecipe_shouldDeleteRecipe() throws Exception {
        Recipe recipe = Recipe.builder()
                .title("recipe-test")
                .instructions("recipe-instructions")
                .category(testCategory)
                .user(testUser)
                .build();
        recipe = recipeRepository.save(recipe);

        String token = jwtUtil.generateToken(testUser);
        mockMvc.perform(delete("/recipes/" + recipe.getId())
                    .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        Recipe deletedRecipe = recipeRepository.findById(recipe.getId()).orElse(null);
        assertNull(deletedRecipe);
    }

    @Test
    void deleteRecipe_shouldFailWhenUserInNotOwner() throws Exception {
        User anotherUser = User.builder()
                            .name("another")
                            .login("another")
                            .passwordHash("password-hash")
                            .build();
        anotherUser = userRepository.save(anotherUser);

        Recipe recipe = Recipe.builder()
                .title("recipe-test")
                .instructions("recipe-instructions")
                .category(testCategory)
                .user(testUser)
                .build();
        recipe = recipeRepository.save(recipe);

        String token = jwtUtil.generateToken(anotherUser);
        mockMvc.perform(delete("/recipes/" + recipe.getId())
                    .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void createRecipe_shouldFailWhenNotAuthorized() throws Exception {
        mockMvc.perform(post("/recipes"))
                .andExpect(status().isForbidden());
    }

    @Test
    void createRecipe_shouldCreateRecipe() throws Exception {
        CreateRecipeRequest createRecipeRequest = new CreateRecipeRequest(
                "new-title",
                "new-instructions",
                testCategory.getId(),
                null,
                null
        );

        String content = objectMapper.writeValueAsString(createRecipeRequest);
        String token = jwtUtil.generateToken(testUser);
        MvcResult result = mockMvc.perform(post("/recipes")
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(createRecipeRequest.title()))
                .andReturn();
        String body = result.getResponse().getContentAsString();
        Long recipeId = objectMapper.readTree(body).get("id").asLong();

        Recipe recipe = recipeRepository.findById(recipeId).orElse(null);
        assertNotNull(recipe);
        assertEquals(createRecipeRequest.title(), recipe.getTitle());
    }

    @Test
    void createRecipe_shouldFailWhenCategoryDoesNotExist() throws Exception {
        CreateRecipeRequest createRecipeRequest = new CreateRecipeRequest("new-title", "new-instructions", -1L, null, null);

        String content = objectMapper.writeValueAsString(createRecipeRequest);
        String token = jwtUtil.generateToken(testUser);
        mockMvc.perform(post("/recipes")
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content))
                .andExpect(status().isNotFound());
    }

    @Test
    void getRecipesByUserId_shouldReturnAllRecipes() throws Exception {
        User anotherUser = User.builder()
                            .name("another")
                            .login("another")
                            .passwordHash("password-hash")
                            .build();
        anotherUser = userRepository.save(anotherUser);

        Recipe recipe1 = Recipe.builder()
                .title("recipe-test-1")
                .instructions("recipe-instructions-1")
                .category(testCategory)
                .user(testUser)
                .build();
        // Рецепт другого пользователя
        Recipe recipe2 = Recipe.builder()
                .title("recipe-test-2")
                .instructions("recipe-instructions-2")
                .category(testCategory)
                .user(anotherUser)
                .build();
        recipe1 = recipeRepository.save(recipe1);
        recipe2 = recipeRepository.save(recipe2);

        mockMvc.perform(get("/users/" + testUser.getId() + "/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(recipe1.getId()));
    }

    @Test
    void getRecipesByUserId_shouldFailWhenUserIdIsIncorrect() throws Exception {
        mockMvc.perform(get("/users/" + -1 + "/recipes"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUserRecipes_shouldReturnAllRecipes() throws Exception {
        User anotherUser = User.builder()
                            .name("another")
                            .login("another")
                            .passwordHash("password-hash")
                            .build();
        anotherUser = userRepository.save(anotherUser);

        Recipe recipe1 = Recipe.builder()
                .title("recipe-test-1")
                .instructions("recipe-instructions-1")
                .category(testCategory)
                .user(testUser)
                .build();
        // Рецепт другого пользователя
        Recipe recipe2 = Recipe.builder()
                .title("recipe-test-2")
                .instructions("recipe-instructions-2")
                .category(testCategory)
                .user(anotherUser)
                .build();
        recipe1 = recipeRepository.save(recipe1);
        recipe2 = recipeRepository.save(recipe2);

        String token = jwtUtil.generateToken(testUser);
        mockMvc.perform(get("/users/me/recipes")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(recipe1.getId()));
    }

    @Test
    void getUserRecipes_shouldFailWhenNotAuthorized() throws Exception {
        mockMvc.perform(get("/users/me/recipes"))
                .andExpect(status().isForbidden());
    }
}
