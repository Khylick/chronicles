package fr.khylick.chronicles.simulation.application;

import java.util.List;

import fr.khylick.chronicles.simulation.domain.CivilizationState;
import fr.khylick.chronicles.world.domain.Territory;
import fr.khylick.chronicles.world.domain.World;

public interface TerritoryExpansionService {
    List<Territory> expand(
        World world,
        List<Territory> territories,
        List<CivilizationState> civilizationStates
    );
}