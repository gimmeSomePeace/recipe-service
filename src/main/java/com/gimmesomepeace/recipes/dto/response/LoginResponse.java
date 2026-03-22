package com.gimmesomepeace.recipes.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Успешный ответ для авторизации пользователя")
public record LoginResponse(
        @Schema(
                description = "JWT access token для авторизации пользователя",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6I..."
        )
        String accessToken,

        // TODO: implement refresh token

        @Schema(
                description = "Тип токена",
                example = "Bearer"
        )
        String tokenType,

        @Schema(
                description = "Время жизни access token в секундах",
                example = "3600"
        )
        long expiresIn,

        @Schema(
                description = "Краткая информация о пользователе"
        )
        UserShortResponse user
) { }
