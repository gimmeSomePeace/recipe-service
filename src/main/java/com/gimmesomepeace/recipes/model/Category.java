package com.gimmesomepeace.recipes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


/**
 * Сущность категории рецепта.
 *
 * Содержит информацию о названии категории.
 */
@Entity
@Table(name = "app_category")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_seq")
    @SequenceGenerator(name = "category_seq", sequenceName = "category_seq", allocationSize = 1)
    private Long id;

    /** Название категории */
    @Column(unique = true, nullable = false)
    @NotBlank
    private String title;
}
