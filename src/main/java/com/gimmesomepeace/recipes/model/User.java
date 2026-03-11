package com.gimmesomepeace.recipes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


/**
 * Сущность пользователя.
 *
 * Содержит информацию о пользователе: имя, логин для авторизации, хеш пароля и его роль.
 *
 */
@Entity
@Table(name = "app_user")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(name = "users_seq", sequenceName = "users_seq", allocationSize = 1)
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

    /** Роль пользователя */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Role role = Role.USER;
}
