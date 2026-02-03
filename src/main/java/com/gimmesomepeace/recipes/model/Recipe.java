package com.gimmesomepeace.recipes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    private String title;

    @Lob
    @Column(nullable = false)
    @NotBlank
    private String instructions;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @NotNull
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User user;

    @Min(0)
    @Max(10)
    private Integer rating;

    @Lob
    private String notes;
}
