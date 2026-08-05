package fr.khylick.chronicles.world.application;

import fr.khylick.chronicles.world.domain.TerrainType;
import fr.khylick.chronicles.world.domain.TileResources;

public interface TileResourcesGenerator {

    TileResources generate(TerrainType terrainType);
}