package com.gimmesomepeace.recipes.dto.response;


import com.gimmesomepeace.recipes.model.User;

public record UserShortResponse(Long id, String login, String name) {
    public static UserShortResponse from(User user) {
        return new UserShortResponse(
                user.getId(),
                user.getLogin(),
                user.getName()
        );
    }
}
