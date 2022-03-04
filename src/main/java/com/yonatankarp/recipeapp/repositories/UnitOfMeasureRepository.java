package com.yonatankarp.recipeapp.repositories;

import java.util.Optional;
import com.yonatankarp.recipeapp.model.UnitOfMeasure;
import org.springframework.data.repository.CrudRepository;

public interface UnitOfMeasureRepository extends CrudRepository<UnitOfMeasure, Long> {
    Optional<UnitOfMeasure> findByDescription(final String description);
}
