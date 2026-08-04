package fr.khylick.chronicles.world.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.random.RandomGenerator;

import fr.khylick.chronicles.world.domain.Position;
import fr.khylick.chronicles.world.domain.TerrainType;
import fr.khylick.chronicles.world.domain.Tile;
import fr.khylick.chronicles.world.domain.World;

public class RandomWorldGenerator implements WorldGenerator {

    private final RandomGenerator randomGenerator;

    public RandomWorldGenerator() {
        this(new Random());
    }

    public RandomWorldGenerator(RandomGenerator randomGenerator) {
        this.randomGenerator = Objects.requireNonNull(
            randomGenerator,
            "Le générateur aléatoire est obligatoire"
        );
    }

    @Override
    public World generate(int width, int height) {
        validateDimensions(width, height);

        List<Tile> tiles = new ArrayList<>(width * height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tiles.add(
                        new Tile(
                                new Position(x, y),
                                generateTerrainType()
                        )
                );
            }
        }

        return new World(width, height, tiles);
    }

    private TerrainType generateTerrainType() {
        int value = randomGenerator.nextInt(100);

        if (value < 40) {
            return TerrainType.OCEAN;
        }

        if (value < 45) {
            return TerrainType.BEACH;
        }

        if (value < 70) {
            return TerrainType.PLAIN;
        }

        if (value < 90) {
            return TerrainType.FOREST;
        }

        if (value < 97) {
            return TerrainType.HILL;
        }

        return TerrainType.MOUNTAIN;
    }

    private static void validateDimensions(int width, int height) {
        if (width <= 0) {
            throw new IllegalArgumentException(
                    "La largeur du monde doit être strictement positive"
            );
        }

        if (height <= 0) {
            throw new IllegalArgumentException(
                    "La hauteur du monde doit être strictement positive"
            );
        }
    }
}