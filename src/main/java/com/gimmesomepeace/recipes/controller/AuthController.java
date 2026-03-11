package com.gimmesomepeace.recipes.controller;

import com.gimmesomepeace.recipes.dto.request.LoginRequest;
import com.gimmesomepeace.recipes.dto.request.RegistrationRequest;
import com.gimmesomepeace.recipes.dto.response.ErrorResponse;
import com.gimmesomepeace.recipes.dto.response.LoginResponse;
import com.gimmesomepeace.recipes.dto.response.UserResponse;
import com.gimmesomepeace.recipes.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Авторизация", description = "Операции, связанные с авторизацией пользователя")
@RestController
@RequestMapping(value = "/auth", produces =  MediaType.APPLICATION_JSON_VALUE)
public class AuthController {
    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @Operation(summary = "Регистрация", description = "Регистрирует пользователя")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Регистрация прошла успешно",
                    content = @Content(
                            schema = @Schema(implementation = UserResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Данные не прошли валидацию",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Логин уже занят",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping("/register")
    ResponseEntity<UserResponse> registration(@Valid @RequestBody RegistrationRequest request) {
        UserResponse user = service.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED) // 201
                .body(user);
    }

    @Operation(summary = "Авторизация", description = "Авторизирует пользователя")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Авторизация прошла успешно",
                    content = @Content(
                            schema = @Schema(implementation = LoginResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Неправильный логин или пароль",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping("/login")
    LoginResponse login(@RequestBody LoginRequest request) {
        return service.login(request);
    }
}
