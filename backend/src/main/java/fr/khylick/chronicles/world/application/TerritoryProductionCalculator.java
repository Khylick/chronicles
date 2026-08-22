package fr.khylick.chronicles.world.application;

import java.util.List;

import fr.khylick.chronicles.world.domain.Territory;
import fr.khylick.chronicles.world.domain.TerritoryProduction;
import fr.khylick.chronicles.world.domain.World;

public interface TerritoryProductionCalculator {

    List<TerritoryProduction> calculate(
        World world,
        List<Territory> territories
    );
}