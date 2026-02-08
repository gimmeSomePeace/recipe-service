package com.gimmesomepeace.recipes.dto.response;

import com.gimmesomepeace.recipes.model.User;

public class UserResponse {
    private final Long id;
    private final String login;
    private final String name;

    public UserResponse(Long id, String login, String name) {
        this.id = id;
        this.login = login;
        this.name = name;
    }

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getLogin(),
                user.getName()
        );
    }
}
