package com.gimmesomepeace.recipes.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


@Schema(description = "Данные для обновлении информации о пользователе")
public record UpdateUserRequest(
        @Schema(description = "Логин пользователя", example = "fat_toad")
        @Size(min = 1, max = 50, message = "Логин не может быть пустым или длиннее 50 символов")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Логин может содержать только буквы, цифры и _")
        String login,

        @Schema(description = "Имя пользователя, отображаемое в UI", example = "Андрей")
        @Size(min = 1, max = 100, message = "Имя не может быть пустым или длиннее 100 символов")
        String name
) { }
