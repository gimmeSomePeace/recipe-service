package com.gimmesomepeace.recipes.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Данные для авторизации пользователя")
public record LoginRequest(
        @Schema(description = "Логин пользователя", example = "fat_toad")
        @NotBlank(message = "Поле 'login' не может быть пустым")
        String login,

        @Schema(description = "Пароль пользователя", example = "ghedUvhGm....")
        @NotBlank(message = "Поле 'password' не может быть пустым")
        String password
) { }
