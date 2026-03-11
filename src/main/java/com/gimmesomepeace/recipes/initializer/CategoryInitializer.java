package com.gimmesomepeace.recipes.initializer;

import com.gimmesomepeace.recipes.model.Category;
import com.gimmesomepeace.recipes.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryInitializer implements CommandLineRunner {
    private final CategoryRepository repository;

    public CategoryInitializer(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        List<String> defaultCategories = List.of(
                "Паста",
                "Гречка",
                "Суп",
                "Выпечка",
                "Десерт",
                "Рис",
                "Что-то необычное"
        );
        for (String title: defaultCategories) {
            if (!repository.existsByTitle(title)) {
                repository.save(Category.builder().title(title).build());
            }
        }
    }
}
