package com.gimmesomepeace.recipes.dto.response;

import com.gimmesomepeace.recipes.model.Role;
import com.gimmesomepeace.recipes.model.User;
import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Данные о пользователе")
public record UserResponse (
        @Schema(description = "Идентификатор пользователя", example = "1")
        Long id,

        @Schema(description = "Логин пользователя", example = "TheQuEuN")
        String login,

        @Schema(description = "Имя пользователя, отображаемое в UI", example = "Сергей")
        String name,

        @Schema(description = "Роль")
        Role role
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getLogin(),
                user.getName()
        );
    }
}
