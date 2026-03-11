package com.gimmesomepeace.recipes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;


/**
 * Сущность пользователя.
 *
 * Содержит информацию о пользователе: имя, логин для авторизации, хеш пароля и его роль.
 *
 */
@Entity
@Table(name = "app_user")
@SQLDelete(sql = "UPDATE app_user set deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
//@FilterDef(name = "activeFilter", defaultCondition = "deleted_at IS NULL")
//@Filter(name = "activeFilter")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
// Сеттеры в данном случае полезны только для PATCH
@Setter
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

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
