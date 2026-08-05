package fr.khylick.chronicles.world.application;

import java.util.List;

import fr.khylick.chronicles.world.domain.Civilization;
import fr.khylick.chronicles.world.domain.World;

public interface CivilizationGenerator {

    List<Civilization> generate(
        World world,
        int civilizationCount
    );
}