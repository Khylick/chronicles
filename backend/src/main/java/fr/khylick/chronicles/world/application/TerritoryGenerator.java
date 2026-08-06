package fr.khylick.chronicles.world.application;

import java.util.List;

import fr.khylick.chronicles.world.domain.Civilization;
import fr.khylick.chronicles.world.domain.Territory;
import fr.khylick.chronicles.world.domain.World;

public interface TerritoryGenerator {

    List<Territory> generate(
        World world,
        List<Civilization> civilizations,
        int maximumDistance
    );
}