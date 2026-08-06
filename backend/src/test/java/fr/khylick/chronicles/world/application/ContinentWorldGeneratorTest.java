package fr.khylick.chronicles.world.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Random;

import fr.khylick.chronicles.world.domain.ResourceType;
import org.junit.jupiter.api.Test;

import fr.khylick.chronicles.world.domain.TerrainType;
import fr.khylick.chronicles.world.domain.World;

class ContinentWorldGeneratorTest {

    @Test
    void shouldGenerateWorldWithRequestedDimensions() {
        WorldGenerator generator =
            new ContinentWorldGenerator(new Random(42));

        World world = generator.generate(20, 12);

        assertThat(world.getWidth()).isEqualTo(20);
        assertThat(world.getHeight()).isEqualTo(12);
        assertThat(world.getTiles()).hasSize(240);
    }

    @Test
    void shouldGenerateOceanOnEveryBorder() {
        WorldGenerator generator =
            new ContinentWorldGenerator(new Random(42));

        World world = generator.generate(20, 12);

        for (int x = 0; x < world.getWidth(); x++) {
            assertThat(
                world.getTile(x, 0).getTerrainType()
            ).isEqualTo(TerrainType.OCEAN);

            assertThat(
                world
                    .getTile(x, world.getHeight() - 1)
                    .getTerrainType()
            ).isEqualTo(TerrainType.OCEAN);
        }

        for (int y = 0; y < world.getHeight(); y++) {
            assertThat(
                world.getTile(0, y).getTerrainType()
            ).isEqualTo(TerrainType.OCEAN);

            assertThat(
                world
                    .getTile(world.getWidth() - 1, y)
                    .getTerrainType()
            ).isEqualTo(TerrainType.OCEAN);
        }
    }

    @Test
    void shouldGenerateSameWorldWithSameSeed() {
        World firstWorld =
            new ContinentWorldGenerator(new Random(42))
                .generate(20, 12);

        World secondWorld =
            new ContinentWorldGenerator(new Random(42))
                .generate(20, 12);

        assertThat(firstWorld.getTiles())
            .extracting(tile -> tile.getTerrainType())
            .containsExactlyElementsOf(
                secondWorld.getTiles()
                    .stream()
                    .map(tile -> tile.getTerrainType())
                    .toList()
                );
    }

    @Test
    void shouldGenerateAtLeastOceanAndLand() {
        WorldGenerator generator =
            new ContinentWorldGenerator(new Random(42));

        World world = generator.generate(40, 30);

        assertThat(world.getTiles())
            .anySatisfy(tile ->
                assertThat(tile.getTerrainType())
                    .isEqualTo(TerrainType.OCEAN)
            );

        assertThat(world.getTiles())
            .anySatisfy(tile ->
                assertThat(tile.getTerrainType())
                    .isNotEqualTo(TerrainType.OCEAN)
            );
    }

    @Test
    void shouldRejectInvalidDimensions() {
        WorldGenerator generator =
            new ContinentWorldGenerator(new Random(42));

        assertThatThrownBy(
            () -> generator.generate(0, 12)
        )
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("largeur");

        assertThatThrownBy(
            () -> generator.generate(20, 0)
        )
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("hauteur");
    }

    @Test
    void shouldGenerateGroupedRelief() {
        World world =
            new ContinentWorldGenerator(new Random(42))
                .generate(40, 24);

        long mountainCount = world.getTiles()
            .stream()
            .filter(tile ->
                tile.getTerrainType()
                    == TerrainType.MOUNTAIN
            )
            .count();

        long hillCount = world.getTiles()
            .stream()
            .filter(tile ->
                tile.getTerrainType()
                    == TerrainType.HILL
            )
            .count();

        assertThat(mountainCount + hillCount)
            .isGreaterThan(0);
    }

    @Test
    void shouldNotGenerateMountainDirectlyNextToOcean() {
        World world =
            new ContinentWorldGenerator(new Random(42))
                .generate(40, 24);

        world.getTiles()
            .stream()
            .filter(tile ->
                tile.getTerrainType()
                    == TerrainType.MOUNTAIN
            )
            .forEach(tile -> {
                int x = tile.getPosition().x();
                int y = tile.getPosition().y();

                int[][] directions = {
                    {0, -1},
                    {1, 0},
                    {0, 1},
                    {-1, 0}
                };

                for (int[] direction : directions) {
                    int neighbourX = x + direction[0];
                    int neighbourY = y + direction[1];

                    if (
                        neighbourX < 0
                            || neighbourX >= world.getWidth()
                            || neighbourY < 0
                            || neighbourY >= world.getHeight()
                    ) {
                        continue;
                    }

                    assertThat(
                        world
                            .getTile(
                                neighbourX,
                                neighbourY
                            )
                            .getTerrainType()
                    ).isNotEqualTo(TerrainType.OCEAN);
                }
            });
    }

    @Test
    void shouldGenerateForests() {
        World world =
            new ContinentWorldGenerator(new Random(42))
                .generate(80, 48);

        long forestCount = world.getTiles()
            .stream()
            .filter(tile ->
                tile.getTerrainType()
                    == TerrainType.FOREST
            )
            .count();

        assertThat(forestCount).isGreaterThan(0);
    }

    @Test
    void shouldNotGenerateForestOnOceanOrBeach() {
        World world =
            new ContinentWorldGenerator(new Random(42))
                .generate(80, 48);

        world.getTiles()
            .stream()
            .filter(tile ->
                tile.getTerrainType()
                    == TerrainType.FOREST
            )
            .forEach(tile ->
                assertThat(tile.getTerrainType())
                    .isNotIn(
                        TerrainType.OCEAN,
                        TerrainType.BEACH
                    )
            );
    }

    @Test
    void shouldNotGenerateForestDirectlyNextToOcean() {
        World world =
            new ContinentWorldGenerator(new Random(42))
                .generate(80, 48);

        world.getTiles()
            .stream()
            .filter(tile ->
                tile.getTerrainType()
                    == TerrainType.FOREST
            )
            .forEach(tile -> {
                int x = tile.getPosition().x();
                int y = tile.getPosition().y();

                int[][] directions = {
                    {0, -1},
                    {1, 0},
                    {0, 1},
                    {-1, 0}
                };

                for (int[] direction : directions) {
                    int neighbourX = x + direction[0];
                    int neighbourY = y + direction[1];

                    if (
                        neighbourX < 0
                            || neighbourX >= world.getWidth()
                            || neighbourY < 0
                            || neighbourY >= world.getHeight()
                    ) {
                        continue;
                    }

                    assertThat(
                        world
                            .getTile(
                                neighbourX,
                                neighbourY
                            )
                            .getTerrainType()
                    ).isNotEqualTo(TerrainType.OCEAN);
                }
            });
    }

    @Test
    void shouldGenerateResourcesForEveryTile() {
        World world =
            new ContinentWorldGenerator(new Random(42))
                .generate(40, 24);

        assertThat(world.getTiles())
            .allSatisfy(tile ->
                assertThat(tile.getResources())
                    .isNotNull()
            );
    }

    @Test
    void shouldGenerateFoodOnPlainTiles() {
        World world =
            new ContinentWorldGenerator(new Random(42))
                .generate(80, 48);

        world.getTiles()
            .stream()
            .filter(tile ->
                tile.getTerrainType()
                    == TerrainType.PLAIN
            )
            .forEach(tile ->
                assertThat(
                    tile.getResources()
                        .get(ResourceType.FOOD)
                ).isEqualTo(4)
            );
    }

    @Test
    void shouldGenerateCivilizations() {
        World world =
            new ContinentWorldGenerator(new Random(42))
                .generate(80, 48);

        assertThat(world.getCivilizations())
            .isNotEmpty();
    }

    @Test
    void shouldCalculateProductionForEveryCivilization() {
        World world =
            new ContinentWorldGenerator(new Random(42))
                .generate(80, 48);

        assertThat(world.getTerritoryProductions())
            .hasSameSizeAs(world.getCivilizations());

        world.getCivilizations()
            .forEach(civilization ->
                assertThat(world.getTerritoryProductions())
                    .anySatisfy(production ->
                        assertThat(
                            production.getCivilizationId()
                        ).isEqualTo(civilization.getId())
                    )
            );
    }

    @Test
    void shouldGeneratePositiveFoodProduction() {
        World world =
            new ContinentWorldGenerator(new Random(42))
                .generate(80, 48);

        assertThat(world.getTerritoryProductions())
            .allSatisfy(production ->
                assertThat(
                    production.get(ResourceType.FOOD)
                ).isPositive()
            );
    }
}