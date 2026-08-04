package fr.khylick.chronicles.world.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Random;

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
}