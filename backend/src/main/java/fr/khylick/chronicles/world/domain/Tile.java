package fr.khylick.chronicles.world.domain;

import java.util.Objects;

public class Tile {

    private final Position position;
    private final TerrainType terrainType;
    private final TileResources resources;

    public Tile(
        Position position,
        TerrainType terrainType,
        TileResources resources
    ) {
        this.position = Objects.requireNonNull(
            position,
            "La position d'une case est obligatoire"
        );

        this.terrainType = Objects.requireNonNull(
            terrainType,
            "Le type de terrain d'une case est obligatoire"
        );

        this.resources = Objects.requireNonNull(
            resources,
            "Les ressources d'une case sont obligatoires"
        );
    }

    public Position getPosition() {
        return position;
    }

    public TerrainType getTerrainType() {
        return terrainType;
    }

    public TileResources getResources() {
        return resources;
    }
}