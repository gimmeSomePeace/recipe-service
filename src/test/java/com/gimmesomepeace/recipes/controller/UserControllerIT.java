package com.gimmesomepeace.recipes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gimmesomepeace.recipes.dto.request.UpdateUserRequest;
import com.gimmesomepeace.recipes.model.User;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerIT {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    TestDatabaseCleaner cleaner;

    User testUser;
    String token;

    @BeforeEach
    void setUp() {
        cleaner.clean();

        recipeRepository.deleteAll();
        userRepository.deleteAll();

        testUser = User.builder()
                .name("name")
                .login("login")
                .passwordHash(passwordEncoder.encode("test-password"))
                .build();
        testUser = userRepository.save(testUser);

        token = jwtUtil.generateToken(testUser);
    }

    @Test
    void getUserInfo_shouldReturnUserData() throws Exception {
        mockMvc.perform(get("/users/me")
                    .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(testUser.getName()))
                .andExpect(jsonPath("$.login").value(testUser.getLogin()));
    }

    @Test
    void getUserInfo_shouldFailWhenNotAuthorized() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateUserInfo_shouldUpdateUserData() throws Exception {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest("new-login", "new-name");
        String content =  objectMapper.writeValueAsString(updateUserRequest);

        mockMvc.perform(patch("/users/me")
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("new-name"))
                .andExpect(jsonPath("$.login").value("new-login"));

        User updatedUser = userRepository.findById(testUser.getId()).orElse(null);
        assertNotNull(updatedUser);
        assertEquals(updatedUser.getName(), testUser.getName());
        assertEquals(updatedUser.getLogin(), testUser.getLogin());
    }

    @Test
    void updateUserInfo_shouldFailWhenNotAuthorized() throws Exception {
        mockMvc.perform(patch("/users/me"))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateUserInfo_shouldFailWhenLoginIsBusy() throws Exception {

        User newUser = User.builder()
                        .name("new-name")
                        .login("new-login")
                        .passwordHash(passwordEncoder.encode("new-password"))
                        .build();
        userRepository.save(newUser);

        UpdateUserRequest updateUserRequest = new UpdateUserRequest("new-login", null);
        String json =  objectMapper.writeValueAsString(updateUserRequest);
        mockMvc.perform(patch("/users/me")
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                .andExpect(status().isConflict());
    }

    @Test
    void deleteUser_shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/users/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
        User deletedUser = userRepository.findById(testUser.getId()).orElse(null);
        assertNull(deletedUser);
    }

    @Test
    void deleteUser_shouldFailWhenNotAuthorized() throws Exception {
        mockMvc.perform(delete("/users/me")).andExpect(status().isForbidden());
    }
}
