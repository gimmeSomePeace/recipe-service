package com.gimmesomepeace.recipes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gimmesomepeace.recipes.dto.request.LoginRequest;
import com.gimmesomepeace.recipes.dto.request.RegistrationRequest;
import com.gimmesomepeace.recipes.model.Role;
import com.gimmesomepeace.recipes.model.User;
import com.gimmesomepeace.recipes.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class AuthControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void registration_shouldRegisterNewUser() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "name",
                "login",
                "some very difficult password"
        );
        String content = objectMapper.writeValueAsString(registrationRequest);

        mockMvc.perform(post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content))
                .andExpect(status().isCreated());
    }

    @Test
    void registration_shouldFailWhenHasEmptyFields() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "name",
                null,
                "some very difficult password"
        );

        String content = objectMapper.writeValueAsString(registrationRequest);
        mockMvc.perform(post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registration_shouldFailWhenLoginBusy() throws Exception {
        User testUser = new User("name", "login", "test password", Role.USER);
        userRepository.save(testUser);

        RegistrationRequest registrationRequest = new RegistrationRequest(
                "name",
                "login",
                "some very difficult password"
        );
        String json = objectMapper.writeValueAsString(registrationRequest);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict());
    }

    @Test
    void login_shouldLogin() throws Exception {

        User testUser = new User("name", "login", encoder.encode("test password"), Role.USER);
        userRepository.save(testUser);

        LoginRequest loginRequest = new LoginRequest("login", "test password");
        String json = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

    @Test
    void login_shouldFailWhenWrongPassword() throws Exception {
        User testUser = new User("name", "login", "test password", Role.USER);
        userRepository.save(testUser);
        LoginRequest loginRequest = new LoginRequest("wrong", "test password");
        String json = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                .andExpect(status().isUnauthorized());
    }
}