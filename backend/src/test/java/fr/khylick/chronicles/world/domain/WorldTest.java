package fr.khylick.chronicles.world.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.Test;

class WorldTest {

    @Test
    void shouldCreateWorldWithExpectedDimensionsAndTiles() {
        List<Tile> tiles = List.of(
            new Tile(new Position(0, 0), TerrainType.OCEAN, TileResources.empty()),
            new Tile(new Position(1, 0), TerrainType.BEACH, TileResources.empty()),
            new Tile(new Position(0, 1), TerrainType.PLAIN, TileResources.empty()),
            new Tile(new Position(1, 1), TerrainType.FOREST, TileResources.empty())
        );

        World world = new World(2, 2, tiles);

        assertThat(world.getWidth()).isEqualTo(2);
        assertThat(world.getHeight()).isEqualTo(2);
        assertThat(world.getTiles()).hasSize(4);
        assertThat(world.getTile(1, 1).getTerrainType())
            .isEqualTo(TerrainType.FOREST);
    }

    @Test
    void shouldRejectWorldWithIncorrectTileCount() {
        List<Tile> tiles = List.of(
            new Tile(new Position(0, 0), TerrainType.OCEAN, TileResources.empty())
        );

        assertThatThrownBy(() -> new World(2, 2, tiles))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("exactement 4 cases");
    }

    @Test
    void shouldRejectDuplicateTilePositions() {
        List<Tile> tiles = List.of(
            new Tile(new Position(0, 0), TerrainType.OCEAN, TileResources.empty()),
            new Tile(new Position(0, 0), TerrainType.BEACH, TileResources.empty())
        );

        assertThatThrownBy(() -> new World(2, 1, tiles))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("même position");
    }

    @Test
    void shouldRejectTileOutsideWorldBounds() {
        List<Tile> tiles = List.of(
            new Tile(new Position(0, 0), TerrainType.OCEAN, TileResources.empty()),
            new Tile(new Position(2, 0), TerrainType.BEACH, TileResources.empty())
        );

        assertThatThrownBy(() -> new World(2, 1, tiles))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("en dehors des limites");
    }
}