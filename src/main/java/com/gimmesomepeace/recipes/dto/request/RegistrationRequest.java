package com.gimmesomepeace.recipes.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


@Schema(description = "Данные для регистрации нового пользователя")
public record RegistrationRequest(
        @Schema(description = "Имя пользователя, отображаемое в UI", example = "Андрей")
        @Size(max = 100, message = "Имя не может быть длиннее 100 символов")
        @NotBlank(message = "Поле name не может быть пустым") String name,

        @Schema(description = "Логин пользователя, используемый для авторизации", example = "fat_toad")
        @Size(max = 50, message = "Логин не может быть длиннее 50 символов")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Логин может содержать только буквы, цифры и _")
        @NotBlank(message = "Поле login не может быть пустым") String login,

        @Schema(description = "Пароль пользователя", example = "very-very-strong-password")
        @Size(min = 8, max = 100, message = "Пароль должен быть от 8 до 100 символов")
        @NotBlank(message = "Поле password не может быть пустым") String password
) { }
