package com.gimmesomepeace.recipes.repository;

import com.gimmesomepeace.recipes.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByTitle(String title);

    Page<Category> findAll(Pageable pageable);
}
