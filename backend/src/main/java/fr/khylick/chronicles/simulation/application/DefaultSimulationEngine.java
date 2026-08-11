package fr.khylick.chronicles.simulation.application;

import java.util.ArrayList;
import java.util.List;

import fr.khylick.chronicles.simulation.domain.CivilizationState;
import fr.khylick.chronicles.simulation.domain.ResourceStock;
import fr.khylick.chronicles.simulation.domain.Simulation;
import fr.khylick.chronicles.world.domain.Civilization;
import fr.khylick.chronicles.world.domain.Population;
import fr.khylick.chronicles.world.domain.ResourceType;
import fr.khylick.chronicles.world.domain.TerritoryProduction;

public final class DefaultSimulationEngine
    implements SimulationEngine {

    @Override
    public Simulation nextTurn(
        Simulation simulation
    ) {
        List<CivilizationState> newStates =
            new ArrayList<>();

        for (
            CivilizationState state :
            simulation.getCivilizationStates()
        ) {
            TerritoryProduction production =
                findProduction(
                    simulation,
                    state
                );

            ResourceStock stock =
                addProduction(
                    state.getStock(),
                    production
                );

            Population population =
                state.getPopulation();

            int foodConsumption =
                population.getFoodConsumptionPerTurn();

            boolean canFeedPopulation =
                stock.get(ResourceType.FOOD)
                    >= foodConsumption;

            if (canFeedPopulation) {
                stock = stock.consume(
                    ResourceType.FOOD,
                    foodConsumption
                );

                population =
                    population.grow();
            }

            newStates.add(
                new CivilizationState(
                    state.getCivilizationId(),
                    population,
                    stock
                )
            );
        }

        return new Simulation(
            simulation.getTurn() + 1,
            simulation.getWorld(),
            newStates
        );
    }

    private TerritoryProduction findProduction(
        Simulation simulation,
        CivilizationState state
    ) {
        return simulation
            .getWorld()
            .getTerritoryProductions()
            .stream()
            .filter(production ->
                production
                    .getCivilizationId()
                    .equals(
                        state.getCivilizationId()
                    )
            )
            .findFirst()
            .orElseThrow(() ->
                new IllegalStateException(
                    "Production introuvable pour "
                        + state.getCivilizationId()
                )
            );
    }

    private ResourceStock addProduction(
        ResourceStock stock,
        TerritoryProduction production
    ) {
        ResourceStock updated = stock;

        for (
            var entry:
            production.getValues().entrySet()
        ) {
            updated = updated.add(
                entry.getKey(),
                entry.getValue()
            );
        }

        return updated;
    }
}