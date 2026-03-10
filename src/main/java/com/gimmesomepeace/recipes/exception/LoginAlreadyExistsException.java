package com.gimmesomepeace.recipes.exception;

public class LoginAlreadyExistsException extends RuntimeException {
    private final String login;

    public LoginAlreadyExistsException(String login) {
        super("Login already exists: " + login);
        this.login = login;
    }

    public String getLogin() {
        return login;
    }
}
