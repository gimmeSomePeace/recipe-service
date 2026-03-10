package com.gimmesomepeace.recipes.exception;


public enum ResourceType {
    USER("USER_NOT_FOUND", "Пользователь"),
    CATEGORY("CATEGORY_NOT_FOUND", "Категория"),
    RECIPE("RECIPE_NOT_FOUND", "Рецепт");

    private final String type;
    private final String title;

    ResourceType(String type, String title) {
        this.type = type;
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }
}
