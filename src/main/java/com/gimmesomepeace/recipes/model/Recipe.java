package com.gimmesomepeace.recipes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;

/**
 * Сущность рецепта.
 *
 * Представляет рецепт блюда, связывается с категорией и пользователем, который его создал.
 * Содержит дополнительные данные, такие как описание, инструкции, оценка и заметки.
 */
@Entity
@Table(name = "app_recipe")
@SQLDelete(sql = "UPDATE app_recipe SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
//@FilterDef(name = "activeFilter", defaultCondition = "deleted_at IS NULL")
//@Filter(name = "activeFilter")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
// Сеттеры в данном случае полезны только для PATCH
@Setter
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recipe_seq")
    @SequenceGenerator(name = "recipe_seq", sequenceName = "recipe_seq",  allocationSize = 1)
    private Long id;

    /** Название блюда */
    @Column(nullable = false)
    @NotBlank
    private String title;

    // TODO: в будущем поменять на адекватную систему с шагами, чем сырой текст
    /** Инструкция по приготовлению блюда */
    @Lob
    @Column(nullable = false)
    @NotBlank
    private String instructions;

    /** Категория, к которой относится блюдо (паста, десерт и т.д.) */
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @NotNull
    private Category category;

    /** Пользователь, владеющий рецептом */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User user;

    /** Рейтинг, который выставил владелец рецепта */
    @Min(0)
    @Max(10)
    @Builder.Default
    private Integer rating = 0;

    /** Заметки, оставленные владельцем рецепта */
    @Lob
    private String notes;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
