package fr.khylick.chronicles.world.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;

import org.junit.jupiter.api.Test;

import fr.khylick.chronicles.world.domain.TerrainType;
import fr.khylick.chronicles.world.domain.World;

class DefaultCivilizationGeneratorTest {

    @Test
    void shouldGenerateRequestedCivilizations() {
        World world =
            new ContinentWorldGenerator(new Random(42))
                .generate(80, 48);

        assertThat(world.getCivilizations())
            .hasSize(4);
    }

    @Test
    void shouldPlaceCapitalsOnSuitableTerrain() {
        World world =
            new ContinentWorldGenerator(new Random(42))
                .generate(80, 48);

        world.getCivilizations()
            .forEach(civilization -> {
                var position =
                    civilization
                        .getCapital()
                        .getPosition();

                var terrainType =
                    world
                        .getTile(
                            position.x(),
                            position.y()
                        )
                        .getTerrainType();

                assertThat(terrainType)
                    .isIn(
                        TerrainType.PLAIN,
                        TerrainType.FOREST
                    );
            });
    }

    @Test
    void shouldPlaceCapitalsAtDistinctPositions() {
        World world =
            new ContinentWorldGenerator(new Random(42))
                .generate(80, 48);

        assertThat(
            world.getCivilizations()
                .stream()
                .map(civilization ->
                    civilization
                        .getCapital()
                        .getPosition()
                )
        ).doesNotHaveDuplicates();
    }

    @Test
    void shouldGeneratePositivePopulationForEveryCapital() {
        World world =
            new ContinentWorldGenerator(new Random(42))
                .generate(80, 48);

        assertThat(world.getCivilizations())
            .allSatisfy(civilization -> {
                var population =
                    civilization
                        .getCapital()
                        .getPopulation();

                assertThat(population.getInhabitants())
                    .isPositive();

                assertThat(population.getGrowthRate())
                    .isPositive();

                assertThat(
                    population.getFoodConsumptionPerTurn()
                ).isPositive();
            });
    }

    @Test
    void shouldGenerateSamePopulationWithSameSeed() {
        World firstWorld =
            new ContinentWorldGenerator(new Random(42))
                .generate(80, 48);

        World secondWorld =
            new ContinentWorldGenerator(new Random(42))
                .generate(80, 48);

        assertThat(firstWorld.getCivilizations())
            .extracting(civilization ->
                civilization
                    .getCapital()
                    .getPopulation()
                    .getInhabitants()
            )
            .containsExactlyElementsOf(
                secondWorld.getCivilizations()
                    .stream()
                    .map(civilization ->
                        civilization
                            .getCapital()
                            .getPopulation()
                            .getInhabitants()
                    )
                    .toList()
            );
    }
}