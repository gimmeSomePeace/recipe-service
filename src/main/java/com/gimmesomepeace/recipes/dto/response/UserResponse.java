package com.gimmesomepeace.recipes.dto.response;

import com.gimmesomepeace.recipes.model.User;

public record UserResponse (Long id, String login, String name) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getLogin(),
                user.getName()
        );
    }
}
