package fr.khylick.chronicles.world.application;

import fr.khylick.chronicles.world.domain.ResourceType;
import fr.khylick.chronicles.world.domain.TerrainType;
import fr.khylick.chronicles.world.domain.TileResources;

public class TerrainTileResourcesGenerator
        implements TileResourcesGenerator {

    @Override
    public TileResources generate(
            TerrainType terrainType
    ) {
        return switch (terrainType) {
            case OCEAN ->
                TileResources.builder()
                    .add(ResourceType.FOOD, 2)
                    .build();

            case BEACH ->
                TileResources.builder()
                    .add(ResourceType.FOOD, 1)
                    .build();

            case PLAIN ->
                TileResources.builder()
                    .add(ResourceType.FOOD, 4)
                    .add(ResourceType.WOOD, 1)
                    .build();

            case FOREST ->
                TileResources.builder()
                    .add(ResourceType.FOOD, 2)
                    .add(ResourceType.WOOD, 4)
                    .build();

            case HILL ->
                TileResources.builder()
                    .add(ResourceType.FOOD, 1)
                    .add(ResourceType.WOOD, 1)
                    .add(ResourceType.STONE, 3)
                    .build();

            case MOUNTAIN ->
                TileResources.builder()
                    .add(ResourceType.STONE, 4)
                    .add(ResourceType.ORE, 3)
                    .build();
        };
    }
}