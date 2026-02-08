package com.gimmesomepeace.recipes.exception;

public class UserNotFoundException extends RuntimeException {
    private final Long id;

    public UserNotFoundException(Long id) {
        super("User not found: " + id);
        this.id = id;
    }

    public Long getLogin() {
        return id;
    }
}
