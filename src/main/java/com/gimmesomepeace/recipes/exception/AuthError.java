package com.gimmesomepeace.recipes.exception;

public enum AuthError {
    INVALID_CREDENTIALS("Неправильный логин или пароль");

    private final String detail;

    AuthError(String detail) {
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }
}
