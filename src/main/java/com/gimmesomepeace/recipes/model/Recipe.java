package com.gimmesomepeace.recipes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Сущность рецепта.
 *
 * Представляет рецепт блюда, связывается с категорией и пользователем, который его создал.
 * Содержит дополнительные данные, такие как описание, инструкции, оценка и заметки.
 */
@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    /** Название блюда */
    @Column(nullable = false)
    @NotBlank
    private String title;

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
    private Integer rating;

    /** Заметки, оставленные владельцем рецепта */
    @Lob
    private String notes;

    // ----- Конструкторы -----
    @SuppressWarnings("unused")
    public Recipe() {}

    public Recipe(String title, String instructions, Category category, User user, Integer rating, String notes) {
        this.title = title;
        this.instructions = instructions;
        this.category = category;
        this.user = user;
        this.rating = rating;
        this.notes = notes;
    }

    // ----- Геттеры ------
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getInstructions() {
        return instructions;
    }

    public Category getCategory() {
        return category;
    }

    public User getUser() {
        return user;
    }

    public Integer getRating() {
        return rating;
    }

    public String getNotes() {
        return notes;
    }

    // ------ Сеттеры ------
    public void setTitle(String title) {
        if (title == null || title.isBlank())
            throw new IllegalArgumentException("Title cannot be null or blank");
        this.title = title;
    }

    public void setInstructions(String instructions) {
        if (instructions == null || instructions.isBlank())
            throw new IllegalArgumentException("instructions cannot be null or blank");
        this.instructions = instructions;
    }

    public void setCategory(Category category) {
        if (category == null) throw new IllegalArgumentException("category cannot be null");
        this.category = category;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
