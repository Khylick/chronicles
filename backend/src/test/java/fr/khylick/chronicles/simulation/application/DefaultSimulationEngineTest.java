package fr.khylick.chronicles.simulation.application;

import static org.assertj.core.api.Assertions.assertThat;

import fr.khylick.chronicles.simulation.domain.Simulation;
import fr.khylick.chronicles.world.application.ContinentWorldGenerator;
import fr.khylick.chronicles.world.domain.Civilization;
import fr.khylick.chronicles.world.domain.ResourceType;
import fr.khylick.chronicles.world.domain.World;
import org.junit.jupiter.api.Test;

import java.util.Random;

class DefaultSimulationEngineTest {

    @Test
    void shouldAdvanceToNextTurn() {
        World world =
            new ContinentWorldGenerator(
                new Random(42)
            ).generate(80, 48);

        Simulation simulation =
            new DefaultSimulationFactory()
                .create(world);

        Simulation next =
            new DefaultSimulationEngine()
                .nextTurn(simulation);

        assertThat(simulation.getTurn())
            .isZero();

        assertThat(next.getTurn())
            .isEqualTo(1);
    }

    @Test
    void shouldProduceResourcesDuringTurn() {
        World world =
            new ContinentWorldGenerator(
                new Random(42)
            ).generate(80, 48);

        Simulation simulation =
            new DefaultSimulationFactory()
                .create(world);

        Simulation next =
            new DefaultSimulationEngine()
                .nextTurn(simulation);

        assertThat(next.getCivilizationStates())
            .allSatisfy(state ->
                assertThat(
                    state.getStock()
                        .get(ResourceType.FOOD)
                ).isPositive()
            );
    }

    @Test
    void shouldGrowPopulationOnlyWhenFoodIsAvailable() {
        World world =
            new ContinentWorldGenerator(
                new Random(42)
            ).generate(80, 48);

        Simulation simulation =
            new DefaultSimulationFactory()
                .create(world);

        Simulation next =
            new DefaultSimulationEngine()
                .nextTurn(simulation);

        for (
            int index = 0;
            index < simulation
                .getCivilizationStates()
                .size();
            index++
        ) {
            var beforeState =
                simulation
                    .getCivilizationStates()
                    .get(index);

            var afterState =
                next
                    .getCivilizationStates()
                    .get(index);

            int beforePopulation =
                beforeState
                    .getPopulation()
                    .getInhabitants();

            int afterPopulation =
                afterState
                    .getPopulation()
                    .getInhabitants();

            int foodConsumption =
                beforeState
                    .getPopulation()
                    .getFoodConsumptionPerTurn();

            int foodProduction =
                world
                    .getTerritoryProductions()
                    .stream()
                    .filter(production ->
                        production
                            .getCivilizationId()
                            .equals(
                                beforeState
                                    .getCivilizationId()
                            )
                    )
                    .findFirst()
                    .orElseThrow()
                    .get(ResourceType.FOOD);

            String civilizationName =
                world.getCivilizations()
                    .stream()
                    .filter(civilization ->
                        civilization.getId()
                            .equals(
                                beforeState.getCivilizationId()
                            )
                    )
                    .map(Civilization::getName)
                    .findFirst()
                    .orElseThrow();

            assertThat(foodProduction)
                .as("production FOOD de %s",
                    civilizationName
                )
                .isGreaterThanOrEqualTo(0);

            System.out.printf(
                "%s : production=%d, consommation=%d, population=%d -> %d%n",
                civilizationName,
                foodProduction,
                foodConsumption,
                beforePopulation,
                afterPopulation
            );

            if (foodProduction >= foodConsumption) {
                assertThat(afterPopulation)
                    .isGreaterThan(beforePopulation);
            } else {
                assertThat(afterPopulation)
                    .isEqualTo(beforePopulation);
            }
        }
    }

    @Test
    void shouldAccumulateResourcesAcrossTurns() {
        World world =
            new ContinentWorldGenerator(
                new Random(42)
            ).generate(80, 48);

        Simulation simulation =
            new DefaultSimulationFactory()
                .create(world);

        Simulation turn1 =
            new DefaultSimulationEngine()
                .nextTurn(simulation);

        Simulation turn2 =
            new DefaultSimulationEngine()
                .nextTurn(turn1);

        assertThat(turn2.getTurn())
            .isEqualTo(2);

        for (int index = 0;
            index < turn1
                .getCivilizationStates()
                .size();
            index++
        ) {

            var stock1 =
                turn1
                    .getCivilizationStates()
                    .get(index)
                    .getStock();

            var stock2 =
                turn2
                    .getCivilizationStates()
                    .get(index)
                    .getStock();

            assertThat(
                stock2.get(ResourceType.WOOD)
            ).isGreaterThan(
                stock1.get(ResourceType.WOOD)
            );
        }
    }
}