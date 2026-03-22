package com.gimmesomepeace.recipes.dto.response;

import com.gimmesomepeace.recipes.model.User;
import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Основные данные о пользователе")
public record UserShortResponse(
        @Schema(description = "Идентификатор пользователя", example = "1")
        Long id,

        @Schema(description = "Логин пользователя", example = "TheQuEuN")
        String login,

        @Schema(description = "Имя пользователя, отображаемое в UI", example = "Сергей")
        String name
) {
    public static UserShortResponse from(User user) {
        return new UserShortResponse(
                user.getId(),
                user.getLogin(),
                user.getName()
        );
    }
}
