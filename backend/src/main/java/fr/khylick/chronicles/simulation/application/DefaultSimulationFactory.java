package fr.khylick.chronicles.simulation.application;

import java.util.List;
import java.util.Random;

import fr.khylick.chronicles.simulation.domain.CivilizationState;
import fr.khylick.chronicles.simulation.domain.ResourceStock;
import fr.khylick.chronicles.simulation.domain.Simulation;
import fr.khylick.chronicles.world.domain.*;

public final class DefaultSimulationFactory
    implements SimulationFactory {

    @Override
    public Simulation create(World world) {
        List<CivilizationState> states =
            world.getCivilizations()
                .stream()
                .map(civilization ->
                    new CivilizationState(
                        civilization.getId(),
                            generateInitialPopulation(
                                world.getTile(
                                    civilization.getCapital().getPosition().x(),
                                    civilization.getCapital().getPosition().y()
                                )
                            ),
                        new ResourceStock()
                    )
                )
                .toList();

        return new Simulation(
            0,
            world,
            states,
            world.getTerritories()
        );
    }

    private Population generateInitialPopulation(
        Tile capitalTile
    ) {
        int foodPotential = capitalTile
            .getResources()
            .get(ResourceType.FOOD);

        int basePopulation = 700;
        int foodBonus = foodPotential * 150;
        int randomBonus = new Random().nextInt(301);

        int inhabitants =
            basePopulation + foodBonus + randomBonus;

        double growthRate =
            0.01 + foodPotential * 0.005;

        return new Population(
            inhabitants,
            growthRate
        );
    }
}