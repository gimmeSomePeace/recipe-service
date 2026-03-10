package com.gimmesomepeace.recipes.exception;

public class LoginFailedException extends RuntimeException {
    private final AuthError error;

    public LoginFailedException(AuthError error) {
        super("Login failed");
        this.error = error;
    }

    public AuthError getError() {
      return error;
    }
}
