package com.gimmesomepeace.recipes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;


@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank
    private String title;

    @OneToMany(mappedBy = "category")
    private List<Recipe> recipes = new ArrayList<>();
}
