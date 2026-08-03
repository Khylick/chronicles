package fr.khylick.chronicles.world.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Random;

import org.junit.jupiter.api.Test;

import fr.khylick.chronicles.world.domain.Position;
import fr.khylick.chronicles.world.domain.World;

class RandomWorldGeneratorTest {

    @Test
    void shouldGenerateWorldWithRequestedDimensions() {
        WorldGenerator generator = new RandomWorldGenerator(new Random(42));

        World world = generator.generate(10, 5);

        assertThat(world.getWidth()).isEqualTo(10);
        assertThat(world.getHeight()).isEqualTo(5);
        assertThat(world.getTiles()).hasSize(50);
    }

    @Test
    void shouldGenerateOneTileForEveryPosition() {
        WorldGenerator generator = new RandomWorldGenerator(new Random(42));

        World world = generator.generate(3, 2);

        assertThat(world.getTiles())
                .extracting(tile -> tile.getPosition())
                .containsExactly(
                        new Position(0, 0),
                        new Position(1, 0),
                        new Position(2, 0),
                        new Position(0, 1),
                        new Position(1, 1),
                        new Position(2, 1)
                );
    }

    @Test
    void shouldGenerateTerrainForEveryTile() {
        WorldGenerator generator = new RandomWorldGenerator(new Random(42));

        World world = generator.generate(10, 10);

        assertThat(world.getTiles())
                .allSatisfy(tile ->
                        assertThat(tile.getTerrainType()).isNotNull()
                );
    }

    @Test
    void shouldGenerateSameWorldWithSameSeed() {
        WorldGenerator firstGenerator =
                new RandomWorldGenerator(new Random(42));

        WorldGenerator secondGenerator =
                new RandomWorldGenerator(new Random(42));

        World firstWorld = firstGenerator.generate(10, 10);
        World secondWorld = secondGenerator.generate(10, 10);

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
    void shouldRejectInvalidWidth() {
        WorldGenerator generator = new RandomWorldGenerator(new Random(42));

        assertThatThrownBy(() -> generator.generate(0, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("largeur");
    }

    @Test
    void shouldRejectInvalidHeight() {
        WorldGenerator generator = new RandomWorldGenerator(new Random(42));

        assertThatThrownBy(() -> generator.generate(10, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("hauteur");
    }
}