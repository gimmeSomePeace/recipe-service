package com.gimmesomepeace.recipes.repository;

import com.gimmesomepeace.recipes.model.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    @Override
    @EntityGraph(attributePaths = {"category", "user"})
    Page<Recipe> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"category", "user"})
    Page<Recipe> findByUserId(Long userId, Pageable pageable);
}
