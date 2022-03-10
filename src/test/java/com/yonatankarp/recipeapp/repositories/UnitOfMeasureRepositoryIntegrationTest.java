package com.yonatankarp.recipeapp.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UnitOfMeasureRepositoryIntegrationTest {

    @Autowired
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @Test
    void findByDescription() {
        final var uomOptional = unitOfMeasureRepository.findByDescription("Teaspoon");
        assertTrue(uomOptional.isPresent());
        assertEquals("Teaspoon", uomOptional.get().getDescription());
    }

    @Test
    void findByDescriptionCup() {
        final var uomOptional = unitOfMeasureRepository.findByDescription("Cup");
        assertTrue(uomOptional.isPresent());
        assertEquals("Cup", uomOptional.get().getDescription());
    }
}
