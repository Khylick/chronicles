package fr.khylick.chronicles.world.domain;

import java.util.List;
import java.util.Objects;

public class World {

    private final int width;
    private final int height;
    private final List<Tile> tiles;
    private final List<Civilization> civilizations;
    private final List<Territory> territories;
    private final List<TerritoryProduction> territoryProductions;

    public World(
        int width,
        int height,
        List<Tile> tiles,
        List<Civilization> civilizations,
        List<Territory> territories,
        List<TerritoryProduction> territoryProductions
    ) {
        if (width <= 0) {
            throw new IllegalArgumentException(
                "La largeur du monde doit être strictement positive"
            );
        };

        if (height <= 0) {
            throw new IllegalArgumentException(
                "La hauteur du monde doit être strictement positive"
            );
        }

        Objects.requireNonNull(
            tiles,
            "La liste des cases du monde est obligatoire"
        );

        Objects.requireNonNull(
            civilizations,
            "La liste des civilisations est obligatoire"
        );

        Objects.requireNonNull(
            territories,
            "La liste des territoires est obligatoire"
        );

        Objects.requireNonNull(
            territoryProductions,
            "La liste des productions est obligatoire"
        );

        int expectedTileCount = width * height;

        if (tiles.size() != expectedTileCount) {
            throw new IllegalArgumentException(
                "Le monde doit contenir exactement %d cases, mais en contient %d"
                    .formatted(expectedTileCount, tiles.size())
            );
        }

        validateTilePosition(width, height, tiles);

        this.width = width;
        this.height = height;
        this.tiles = List.copyOf(tiles);
        this.civilizations = List.copyOf(civilizations);
        this.territories = List.copyOf(territories);
        this.territoryProductions = List.copyOf(territoryProductions);
    }

    public World(
        int width,
        int height,
        List<Tile> tiles
    ) {
        this(
            width,
            height,
            tiles,
            List.of(),
            List.of(),
            List.of()
        );
    }

    public World(
        int width,
        int height,
        List<Tile> tiles,
        List<Civilization> civilizations
    ) {
        this(
            width,
            height,
            tiles,
            civilizations,
            List.of(),
            List.of()
        );
    }

    public World(
        int width,
        int height,
        List<Tile> tiles,
        List<Civilization> civilizations,
        List<Territory> territories
    ) {
        this(
            width,
            height,
            tiles,
            civilizations,
            territories,
            List.of()
        );
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public Tile getTile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException(
                "Aucune case trouvée à la position (%d, %d)"
                    .formatted(x, y)
            );
        }

        return tiles.get(y * width + x);
    }

    public List<Civilization> getCivilizations() {
        return civilizations;
    }

    public List<Territory> getTerritories() {
        return territories;
    }

    public List<TerritoryProduction> getTerritoryProductions() {
        return territoryProductions;
    }

    private static void validateTilePosition(
        int width,
        int height,
        List<Tile> tiles
    ) {
        long distinctPositionCount = tiles.stream()
            .map(Tile::getPosition)
            .distinct()
            .count();

        if (distinctPositionCount != tiles.size()) {
            throw new IllegalArgumentException(
                "Le monde ne peut pas contenir plusieurs cases à la même position"
            );
        }

        boolean containsOutOfBoundsPosition = tiles.stream()
            .map(Tile::getPosition)
            .anyMatch(position ->
                position.x() >= width
                    || position.y() >= height
            );

        if (containsOutOfBoundsPosition) {
            throw new IllegalArgumentException(
                "Une ou plusieurs cases se trouvent en dehors des limites du monde"
            );
        }
    }
}