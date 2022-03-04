package com.yonatankarp.recipeapp.repositories;

import java.util.Optional;
import com.yonatankarp.recipeapp.model.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {
    Optional<Category> findByDescription(final String description);
}
