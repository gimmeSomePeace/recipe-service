package com.gimmesomepeace.recipes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;


/**
 * Сущность категории рецепта.
 *
 * Содержит информацию о названии категории.
 * Также содержит список рецептов, относящихся к рассматриваемой категории.
 */
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    /** Название категории */
    @Column(unique = true, nullable = false)
    @NotBlank
    private String title;

    /** Список рецептов, относящихся к данной категории */
    @OneToMany(mappedBy = "category")
    private List<Recipe> recipes = new ArrayList<>();
}
