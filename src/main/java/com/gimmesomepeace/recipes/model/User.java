package com.gimmesomepeace.recipes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;


@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String login;

    @NotBlank
    @Column(nullable = false)
    private String passwordHash;

    @OneToMany(mappedBy = "user")
    List<Recipe> recipes = new ArrayList<>();
}
