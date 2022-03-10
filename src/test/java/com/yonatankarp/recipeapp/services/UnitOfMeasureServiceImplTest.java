package com.yonatankarp.recipeapp.services;

import java.util.HashSet;
import com.yonatankarp.recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.yonatankarp.recipeapp.model.UnitOfMeasure;
import com.yonatankarp.recipeapp.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UnitOfMeasureServiceImplTest {

    private final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand = new UnitOfMeasureToUnitOfMeasureCommand();

    @Mock
    private UnitOfMeasureRepository unitOfMeasureRepository;

    private UnitOfMeasureService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        service = new UnitOfMeasureServiceImpl(unitOfMeasureRepository, unitOfMeasureToUnitOfMeasureCommand);
    }

    @Test
    void listAllUoms() {
        // Given
        final var unitOfMeasures = new HashSet<UnitOfMeasure>() {{
           add(UnitOfMeasure.builder().id(1L).build());
           add(UnitOfMeasure.builder().id(2L).build());
        }};

        when(unitOfMeasureRepository.findAll()).thenReturn(unitOfMeasures);

        // When
        final var commands = service.listAllUoms();

        // Then
        assertEquals(2, commands.size());
        verify(unitOfMeasureRepository).findAll();
    }
}
