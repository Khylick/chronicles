package fr.khylick.chronicles.world.application;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import fr.khylick.chronicles.world.domain.ResourceType;
import fr.khylick.chronicles.world.domain.TerrainType;
import fr.khylick.chronicles.world.domain.TileResources;

class TerrainTileResourcesGeneratorTest {

    private final TileResourcesGenerator generator =
        new TerrainTileResourcesGenerator();

    @Test
    void shouldGenerateFoodForPlain() {
        TileResources resources =
            generator.generate(TerrainType.PLAIN);

        assertThat(resources.get(ResourceType.FOOD))
            .isEqualTo(4);
    }

    @Test
    void shouldGenerateWoodForForest() {
        TileResources resources =
            generator.generate(TerrainType.FOREST);

        assertThat(resources.get(ResourceType.WOOD))
            .isEqualTo(4);
    }

    @Test
    void shouldGenerateStoneAndOreForMountain() {
        TileResources resources =
            generator.generate(TerrainType.MOUNTAIN);

        assertThat(resources.get(ResourceType.STONE))
            .isEqualTo(4);

        assertThat(resources.get(ResourceType.ORE))
            .isEqualTo(3);
    }

    @Test
    void shouldNotGenerateOreForPlain() {
        TileResources resources =
            generator.generate(TerrainType.PLAIN);

        assertThat(resources.get(ResourceType.ORE))
            .isZero();
    }
}