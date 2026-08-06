package fr.khylick.chronicles.world.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.Test;

import fr.khylick.chronicles.world.domain.Position;
import fr.khylick.chronicles.world.domain.TerrainType;
import fr.khylick.chronicles.world.domain.World;

class InitialTerritoryGeneratorTest {

    @Test
    void shouldGenerateOneTerritoryPerCivilization() {
        World world =
            new ContinentWorldGenerator(new Random(42))
                .generate(80, 48);

        assertThat(world.getTerritories())
            .hasSameSizeAs(world.getCivilizations());
    }

    @Test
    void shouldIncludeCapitalInCivilizationTerritory() {
        World world =
            new ContinentWorldGenerator(new Random(42))
                .generate(80, 48);

        world.getCivilizations()
            .forEach(civilization -> {
                Position capitalPosition =
                    civilization
                        .getCapital()
                        .getPosition();

                var territory = world.getTerritories()
                    .stream()
                    .filter(candidate ->
                        candidate
                            .getCivilizationId()
                            .equals(civilization.getId())
                    )
                    .findFirst()
                    .orElseThrow();

                assertThat(
                    territory.contains(capitalPosition)
                ).isTrue();
            });
    }

    @Test
    void shouldNotClaimSamePositionTwice() {
        World world =
            new ContinentWorldGenerator(new Random(42))
                .generate(80, 48);

        Set<Position> claimedPositions =
            new HashSet<>();

        world.getTerritories()
            .forEach(territory ->
                territory.getPositions()
                    .forEach(position ->
                        assertThat(
                            claimedPositions.add(position)
                        ).isTrue()
                    )
            );
    }

    @Test
    void shouldNotClaimOceanOrMountain() {
        World world =
            new ContinentWorldGenerator(new Random(42))
                .generate(80, 48);

        world.getTerritories()
            .forEach(territory ->
                territory.getPositions()
                    .forEach(position -> {
                        TerrainType terrainType =
                            world.getTile(
                                position.x(),
                                position.y()
                            ).getTerrainType();

                        assertThat(terrainType)
                            .isNotIn(
                                TerrainType.OCEAN,
                                TerrainType.MOUNTAIN
                            );
                    })
            );
    }
}