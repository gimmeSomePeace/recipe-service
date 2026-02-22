package com.gimmesomepeace.recipes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;


/**
 * Сущность пользователя.
 *
 * Содержит информацию о пользователе: имя, логин для авторизации и хеш пароля.
 * Также содержит список рецептов, созданных пользователем.
 */
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    /** Имя пользователя, отображаемое в UI */
    @Column(nullable = false)
    @NotBlank
    private String name;

    /** Логин пользователя, используется для авторизации. */
    @Column(unique = true, nullable = false)
    @NotBlank
    private String login;

    /** Хеш пароля пользователя. */
    @Column(nullable = false)
    @NotBlank
    private String passwordHash;

    /** Все рецепты, созданные пользователем */
    @OneToMany(mappedBy = "user")
    List<Recipe> recipes = new ArrayList<>();

    private Role role;

    // ----- Конструкторы -----
    @SuppressWarnings("unused")
    protected User() {}

    public User(String name, String login, String passwordHash, Role role) {
        this.name = name;
        this.login = login;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    // ----- Геттеры -----

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Role getRole() {
        return role;
    }

    // ----- Сеттеры -----

    public void setName(String name) {
        this.name = name;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
