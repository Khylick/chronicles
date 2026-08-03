package fr.khylick.chronicles.world.domain;

import java.util.Objects;

public class Tile {

    private final Position position;
    private final TerrainType terrainType;

    public Tile(Position position, TerrainType terrainType) {
        this.position = Objects.requireNonNull(
            position,
            "La position d'une case est obligatoire"
        );

        this.terrainType = Objects.requireNonNull(
            terrainType,
            "Le type de terrain d'une case est obligatoire"
        );
    }

    public Position getPosition() {
        return position;
    }

    public TerrainType getTerrainType() {
        return terrainType;
    }
}