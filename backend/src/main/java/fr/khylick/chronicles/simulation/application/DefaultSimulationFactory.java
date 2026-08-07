package fr.khylick.chronicles.simulation.application;

import java.util.List;

import fr.khylick.chronicles.simulation.domain.CivilizationState;
import fr.khylick.chronicles.simulation.domain.ResourceStock;
import fr.khylick.chronicles.simulation.domain.Simulation;
import fr.khylick.chronicles.world.domain.World;

public final class DefaultSimulationFactory
    implements SimulationFactory {

    @Override
    public Simulation create(World world) {
        List<CivilizationState> states =
            world.getCivilizations()
                .stream()
                .map(civilization ->
                    new CivilizationState(
                        civilization,
                        new ResourceStock()
                    )
                )
                .toList();

        return new Simulation(
            0,
            world,
            states
        );
    }
}