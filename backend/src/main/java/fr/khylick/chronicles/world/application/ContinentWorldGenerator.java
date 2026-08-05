package fr.khylick.chronicles.world.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.random.RandomGenerator;

import fr.khylick.chronicles.world.domain.*;

public class ContinentWorldGenerator implements WorldGenerator {

    // 3 : Pour davantage de détails
    // 4 : Réglage de base
    // 5 : Pour des masses plus lisses
    private static final int SMOOTHING_PASSES = 5;

    private static final int ELEVATION_SMOOTHING_PASSES = 2;
    private static final int MOISTURE_SMOOTHING_PASSES = 3;

    private static final double MOUNTAIN_THRESHOLD = 0.78;
    private static final double HILL_THRESHOLD = 0.62;
    private static final double FOREST_THRESHOLD = 0.58;

    private final RandomGenerator randomGenerator;

    private final TileResourcesGenerator tileResourcesGenerator;

    public ContinentWorldGenerator() {
        this(
            new Random(),
            new TerrainTileResourcesGenerator()
        );
    }

    public ContinentWorldGenerator(
        RandomGenerator randomGenerator
    ) {
        this(
            randomGenerator,
            new TerrainTileResourcesGenerator()
        );
    }

    public ContinentWorldGenerator(
        RandomGenerator randomGenerator,
        TileResourcesGenerator tileResourcesGenerator
    ) {
        this.randomGenerator = Objects.requireNonNull(
            randomGenerator,
            "Le générateur aléatoire est obligatoire"
        );

        this.tileResourcesGenerator =
            Objects.requireNonNull(
                tileResourcesGenerator,
                "Le générateur de ressources est obligatoire"
            );
    }

    @Override
    public World generate(int width, int height) {
        validateDimensions(width, height);

        boolean[][] landMap = generateInitialLandMap(width, height);

        for (int pass = 0; pass < SMOOTHING_PASSES; pass++) {
            landMap = smoothLandMap(landMap, width, height);
        }

        double[][] elevationMap = generateElevationMap(
            landMap,
            width,
            height
        );

        for (
            int pass = 0;
            pass < ELEVATION_SMOOTHING_PASSES;
            pass++
        ) {
            elevationMap = smoothScalarMap(
                elevationMap,
                landMap,
                width,
                height
            );
        }

        double[][] moistureMap = generateMoistureMap(
            landMap,
            width,
            height
        );

        for (
            int pass = 0;
            pass < MOISTURE_SMOOTHING_PASSES;
            pass++
        ) {
            moistureMap = smoothScalarMap(
                moistureMap,
                landMap,
                width,
                height
            );
        }

        List<Tile> tiles = generateTiles(
            landMap,
            elevationMap,
            moistureMap,
            width,
            height
        );

        return new World(width, height, tiles);
    }

    private boolean[][] generateInitialLandMap(
        int width,
        int height
    ) {
        boolean[][] landMap = new boolean[height][width];

        double centerX = (width - 1) / 2.0;
        double centerY = (height - 1) / 2.0;
        double maximumDistance = Math.sqrt(
            centerX * centerX + centerY * centerY
        );

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double distanceFromCenter = Math.sqrt(
                    Math.pow(x - centerX, 2)
                        + Math.pow(y - centerY, 2)
                );

                double normalizedDistance =
                    distanceFromCenter / maximumDistance;

                // Réglage de base
                //double landProbability = 0.72 - normalizedDistance * 0.52;
                // Pour davantage de terre :
                double landProbability = 0.80 - normalizedDistance * 0.50;
                // Pour davantage d'océan :
                // double landProbability = 0.65 - normalizedDistance * 0.55;

                landMap[y][x] =
                    randomGenerator.nextDouble()
                        < landProbability;
            }
        }

        forceOceanBorders(landMap, width, height);

        return landMap;
    }

    private boolean[][] smoothLandMap(
        boolean[][] currentMap,
        int width,
        int height
    ) {
        boolean[][] smoothedMap
            = new boolean[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (isBorder(x, y, width, height)) {
                    smoothedMap[y][x] = false;
                    continue;
                }

                int landNeighbourCount =
                    countLandNeighbours(
                        currentMap,
                        x,
                        y,
                        width,
                        height
                    );

                if (landNeighbourCount > 4) {
                    smoothedMap[y][x] = true;
                } else if (landNeighbourCount < 4) {
                    smoothedMap[y][x] = false;
                } else {
                    smoothedMap[y][x] = currentMap[y][x];
                }
            }
        }

        return smoothedMap;
    }

    private List<Tile> generateTiles(
        boolean[][] landMap,
        double[][] elevationMap,
        double[][] moistureMap,
        int width,
        int height
    ) {
        List<Tile> tiles =
            new ArrayList<>(width * height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                TerrainType terrainType =
                    determineTerrainType(
                        landMap,
                        elevationMap,
                        moistureMap,
                        x,
                        y,
                        width,
                        height
                    );

                TileResources resources =
                    tileResourcesGenerator.generate(terrainType);

                tiles.add(
                    new Tile(
                        new Position(x, y),
                        terrainType,
                        resources
                    )
                );
            }
        }

        return tiles;
    }

    private static TerrainType determineTerrainType(
            boolean[][] landMap,
            double[][] elevationMap,
            double[][] moistureMap,
            int x,
            int y,
            int width,
            int height
    ) {
        if (!landMap[y][x]) {
            return TerrainType.OCEAN;
        }

        if (hasAdjacentOcean(
                landMap,
                x,
                y,
                width,
                height
        )) {
            return TerrainType.BEACH;
        }

        double elevation = elevationMap[y][x];

        // Régler le paramêtre pour plus ou moins de montagnes.
        if (elevation >= MOUNTAIN_THRESHOLD) {
            return TerrainType.MOUNTAIN;
        }

        // Régler le paramêtre pour plus ou moins de plaines.
        if (elevation >= HILL_THRESHOLD) {
            return TerrainType.HILL;
        }

        double moisture = moistureMap[y][x];

        if (moisture >= FOREST_THRESHOLD) {
            return TerrainType.FOREST;
        }

        return TerrainType.PLAIN;
    }

    private static int countLandNeighbours(
        boolean[][] landMap,
        int x,
        int y,
        int width,
        int height
    ) {
        int count = 0;

        for (int offsetY = -1; offsetY <= 1; offsetY++) {
            for (int offsetX = -1; offsetX <= 1; offsetX++) {
                if (offsetX == 0 && offsetY == 0) {
                    continue;
                }

                int neighbourX = x + offsetX;
                int neighbourY = y + offsetY;

                if (
                    neighbourX >= 0
                        && neighbourX < width
                        && neighbourY >= 0
                        && neighbourY < height
                        && landMap[neighbourY][neighbourX]
                ) {
                    count++;
                }
            }
        }

        return count;
    }

    private static boolean hasAdjacentOcean(
        boolean[][] landMap,
        int x,
        int y,
        int width,
        int height
    ) {
        int[][] directions = {
            {0, -1},
            {1, 0},
            {0, 1},
            {-1, 0}
        };

        for (int[] direction : directions) {
            int neighbourX = x + direction[0];
            int neighbourY = y + direction[1];

            if (
                neighbourX < 0
                || neighbourX >= width
                || neighbourY < 0
                || neighbourY >= height
            ) {
                return true;
            }

            if (!landMap[neighbourY][neighbourX]) {
                return true;
            }
        }

        return false;
    }

    private static void forceOceanBorders(
        boolean[][] landMap,
        int width,
        int height
    ) {
        for (int x = 0; x < width; x++) {
            landMap[0][x] = false;
            landMap[height - 1][x] = false;
        }

        for (int y = 0; y < height; y++) {
            landMap[y][0] = false;
            landMap[y][width - 1] = false;
        }
    }

    private static boolean isBorder(
        int x,
        int y,
        int width,
        int height
    ) {
        return x == 0
            || y == 0
            || x == width - 1
            || y == height - 1;
    }

    private static void validateDimensions(
        int width,
        int height
    ) {
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

    private double[][] generateElevationMap(
        boolean[][] landMap,
        int width,
        int height
    ) {
        double[][] elevationMap =
            new double[height][width];

        double centerX = (width - 1) / 2.0;
        double centerY = (height - 1) / 2.0;
        double maximumDistance = Math.sqrt(
            centerX * centerX + centerY * centerY
        );

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (!landMap[y][x]) {
                    elevationMap[y][x] = 0.0;
                    continue;
                }

                double distanceFromCenter = Math.sqrt(
                    Math.pow(x - centerX, 2)
                        + Math.pow(y - centerY, 2)
                );

                double normalizedDistance =
                    distanceFromCenter / maximumDistance;

                double continentalFactor =
                    1.0 - normalizedDistance;

                double randomFactor =
                    randomGenerator.nextDouble();

                elevationMap[y][x] =
                    continentalFactor * 0.50
                        + randomFactor * 0.50;
            }
        }

        return elevationMap;
    }

    private static double[][] smoothScalarMap(
        double[][] currentMap,
        boolean[][] landMap,
        int width,
        int height
    ) {
        double[][] smoothedMap =
            new double[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (!landMap[y][x]) {
                    smoothedMap[y][x] = 0.0;
                    continue;
                }

                double weightedTotal =
                    currentMap[y][x] * 2.0;
                double totalWeight = 2.0;

                for (
                    int offsetY = -1;
                    offsetY <= 1;
                    offsetY++
                ) {
                    for (
                        int offsetX = -1;
                        offsetX <= 1;
                        offsetX++
                    ) {
                        if (offsetX == 0 && offsetY == 0) {
                            continue;
                        }

                        int neighbourX = x + offsetX;
                        int neighbourY = y + offsetY;

                        if (
                            neighbourX < 0
                                || neighbourX >= width
                                || neighbourY < 0
                                || neighbourY >= height
                                || !landMap[neighbourY][neighbourX]
                        ) {
                            continue;
                        }

                        weightedTotal += currentMap[neighbourY][neighbourX];
                        totalWeight += 1.0;
                    }
                }

                smoothedMap[y][x] = weightedTotal / totalWeight;
            }
        }

        return smoothedMap;
    }

    private double[][] generateMoistureMap(
        boolean[][] landMap,
        int width,
        int height
    ) {
        double[][] moistureMap =
            new double[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (!landMap[y][x]) {
                    moistureMap[y][x] = 0.0;
                    continue;
                }

                double coastalInfluence =
                    calculateCoastalInfluence(
                        landMap,
                        x,
                        y,
                        width,
                        height
                    );

                double randomFactor =
                    randomGenerator.nextDouble();

                moistureMap[y][x] =
                    clamp(
                        coastalInfluence * 0.45
                            + randomFactor * 0.55
                    );
            }
        }

        return moistureMap;
    }

    private static double calculateCoastalInfluence(
        boolean[][] landMap,
        int x,
        int y,
        int width,
        int height
    ) {
        final int searchRadius = 4;
        int minimumOceanDistance = searchRadius + 1;

        for (
            int offsetY = -searchRadius;
            offsetY <= searchRadius;
            offsetY++
        ) {
            for (
                int offsetX = -searchRadius;
                offsetX <= searchRadius;
                offsetX++
            ) {
                int distance =
                    Math.abs(offsetX) + Math.abs(offsetY);

                if (distance == 0 || distance > searchRadius) {
                    continue;
                }

                int neighbourX = x + offsetX;
                int neighbourY = y + offsetY;

                // L'extérieur de la carte est considéré comme de l'océan.
                if (
                    neighbourX < 0
                        || neighbourX >= width
                        || neighbourY < 0
                        || neighbourY >= height
                ) {
                    minimumOceanDistance = Math.min(
                        minimumOceanDistance,
                        distance
                    );

                    continue;
                }

                if (!landMap[neighbourY][neighbourX]) {
                    minimumOceanDistance = Math.min(
                        minimumOceanDistance,
                        distance
                    );
                }
            }
        }

        if (minimumOceanDistance > searchRadius) {
            return 0.0;
        }

        return 1.0
            - (minimumOceanDistance - 1.0)
                / searchRadius;
    }

    private static double clamp(double value) {
        return Math.max(0.0, Math.min(1.0, value));
    }
}