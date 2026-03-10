package com.gimmesomepeace.recipes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gimmesomepeace.recipes.dto.request.UpdateUserRequest;
import com.gimmesomepeace.recipes.model.Role;
import com.gimmesomepeace.recipes.model.User;
import com.gimmesomepeace.recipes.repository.RecipeRepository;
import com.gimmesomepeace.recipes.repository.UserRepository;
import com.gimmesomepeace.recipes.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    User testUser;
    String token;

    @BeforeEach
    void setUp() {
        recipeRepository.deleteAll();
        userRepository.deleteAll();
        testUser = new User("name", "login", passwordEncoder.encode("test-password"), Role.USER);
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
        String json =  objectMapper.writeValueAsString(updateUserRequest);
        mockMvc.perform(patch("/users/me")
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("new-name"))
                .andExpect(jsonPath("$.login").value("new-login"));
    }

    @Test
    void updateUserInfo_shouldFailWhenNotAuthorized() throws Exception {
        mockMvc.perform(patch("/users/me"))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateUserInfo_shouldFailWhenLoginIsBusy() throws Exception {
        User newUser = new User("new-user", "new-user", "passsword-hash", Role.USER);
        userRepository.save(newUser);

        UpdateUserRequest updateUserRequest = new UpdateUserRequest("new-user", null);
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
    }

    @Test
    void deleteUser_shouldFailWhenNotAuthorized() throws Exception {
        mockMvc.perform(delete("/users/me")).andExpect(status().isForbidden());
    }
}
