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
}
