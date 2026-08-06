package fr.khylick.chronicles.world.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import fr.khylick.chronicles.world.domain.Position;
import fr.khylick.chronicles.world.domain.ResourceType;
import fr.khylick.chronicles.world.domain.TerrainType;
import fr.khylick.chronicles.world.domain.Territory;
import fr.khylick.chronicles.world.domain.Tile;
import fr.khylick.chronicles.world.domain.TileResources;
import fr.khylick.chronicles.world.domain.World;

class DefaultTerritoryProductionCalculatorTest {

    private final TerritoryProductionCalculator calculator =
        new DefaultTerritoryProductionCalculator();

    @Test
    void shouldSumResourcesFromTerritoryTiles() {
        UUID civilizationId = UUID.randomUUID();

        List<Tile> tiles = List.of(
            new Tile(
                new Position(0, 0),
                TerrainType.PLAIN,
                new TileResources(
                    Map.of(
                        ResourceType.FOOD, 4,
                        ResourceType.WOOD, 1
                    )
                )
            ),
            new Tile(
                new Position(1, 0),
                    TerrainType.FOREST,
                    new TileResources(
                        Map.of(
                            ResourceType.FOOD, 2,
                            ResourceType.WOOD, 4
                        )
                    )
            )
        );

        Territory territory = new Territory(
            civilizationId,
            Set.of(
                new Position(0, 0),
                new Position(1, 0)
            )
        );

        World world = new World(
            2,
            1,
            tiles,
            List.of(),
            List.of(territory)
        );

        var productions = calculator.calculate(world);

        assertThat(productions).hasSize(1);

        var production = productions.getFirst();

        assertThat(production.getCivilizationId())
            .isEqualTo(civilizationId);

        assertThat(production.get(ResourceType.FOOD))
            .isEqualTo(6);

        assertThat(production.get(ResourceType.WOOD))
            .isEqualTo(5);

        assertThat(production.get(ResourceType.STONE))
            .isZero();
    }

    @Test
    void shouldGenerateOneProductionPerTerritory() {
        World world =
            new ContinentWorldGenerator(new java.util.Random(42))
                .generate(80, 48);

        assertThat(world.getTerritoryProductions())
            .hasSameSizeAs(world.getTerritories());
    }
}