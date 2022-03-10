package com.yonatankarp.recipeapp.services;

import java.util.Set;
import com.yonatankarp.recipeapp.commands.UnitOfMeasureCommand;

public interface UnitOfMeasureService {
    Set<UnitOfMeasureCommand> listAllUoms();
}
