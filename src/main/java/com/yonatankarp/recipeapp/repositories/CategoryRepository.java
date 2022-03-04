package com.yonatankarp.recipeapp.repositories;

import com.yonatankarp.recipeapp.model.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {
}
