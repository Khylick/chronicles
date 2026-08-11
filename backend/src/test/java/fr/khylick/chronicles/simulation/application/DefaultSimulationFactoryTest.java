package fr.khylick.chronicles.simulation.application;

import fr.khylick.chronicles.simulation.domain.Simulation;
import fr.khylick.chronicles.world.application.ContinentWorldGenerator;
import fr.khylick.chronicles.world.domain.Population;
import fr.khylick.chronicles.world.domain.World;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class DefaultSimulationFactoryTest {

    @Test
    void shouldCreatePositivePopulationForEveryCivilizationState() {
        World world =
            new ContinentWorldGenerator(new Random(42))
                .generate(80, 48);

        Simulation simulation =
            new DefaultSimulationFactory()
                .create(world);

        assertThat(simulation.getCivilizationStates())
            .allSatisfy(state -> {
                Population population =
                    state.getPopulation();

                assertThat(population.getInhabitants())
                    .isPositive();

                assertThat(population.getGrowthRate())
                    .isPositive();

                assertThat(
                    population.getFoodConsumptionPerTurn()
                ).isPositive();
            });
    }
}