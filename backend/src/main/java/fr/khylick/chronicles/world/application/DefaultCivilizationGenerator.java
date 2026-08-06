package fr.khylick.chronicles.world.application;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.random.RandomGenerator;

import fr.khylick.chronicles.world.domain.*;

public class DefaultCivilizationGenerator
    implements CivilizationGenerator {

    private static final int MINIMUM_DISTANCE = 8;

    private static final List<String> CIVILIZATION_NAMES =
        List.of(
            "Aldéens",
            "Valoriens",
            "Eryndiens",
            "Solariens",
            "Thalassiens",
            "Kaelites",
            "Oréans",
            "Myréens"
        );

    private static final List<String> CAPITAL_NAMES =
        List.of(
            "Aldor",
            "Valoria",
            "Eryndor",
            "Solenne",
            "Thalès",
            "Kaelis",
            "Oria",
            "Myren"
        );

    private static final List<String> COLORS =
        List.of(
            "#e63946",
            "#ffb703",
            "#8ecae6",
            "#8338ec",
            "#fb8500",
            "#2a9d8f",
            "#90be6d"
        );

    private final RandomGenerator randomGenerator;

    public DefaultCivilizationGenerator() {
        this(new Random());
    }

    public DefaultCivilizationGenerator(
        RandomGenerator randomGenerator
    ) {
        this.randomGenerator = Objects.requireNonNull(
            randomGenerator,
            "Le générateur aléatoire est obligatoire"
        );
    }

    @Override
    public List<Civilization> generate(
        World world,
        int civilizationCount
    ) {
        if (civilizationCount <= 0) {
            throw new IllegalArgumentException(
                "Le nombre de civilisations doit être strictement positif"
            );
        }

        List<Tile> candidates = world.getTiles()
            .stream()
            .filter(this::isSuitableCapitalTile)
            .sorted(
                Comparator.comparingInt(this::scoreTile)
                    .reversed()
            )
            .toList();

        List<Civilization> civilizations =
            new ArrayList<>();

        List<Position> selectedPositions =
            new ArrayList<>();

        for (Tile candidate : candidates) {
            if (civilizations.size() >= civilizationCount) {
                break;
            }

            Position position = candidate.getPosition();

            if (!isFarEnough(
                position,
                selectedPositions
            )) {
                continue;
            }

            int index = civilizations.size();

            String civilizationName =
                CIVILIZATION_NAMES.get(
                    index % CIVILIZATION_NAMES.size()
                );

            String capitalName =
                CAPITAL_NAMES.get(
                    index % CAPITAL_NAMES.size()
                );

            String color =
                COLORS.get(index % COLORS.size());

            Population population =
                generateInitialPopulation(candidate);

            Capital capital = new Capital(
                UUID.randomUUID(),
                capitalName,
                position,
                population
            );

            civilizations.add(
                new Civilization(
                    UUID.randomUUID(),
                    civilizationName,
                    color,
                    capital
                )
            );

            selectedPositions.add(position);
        }

        return List.copyOf(civilizations);
    }

    private boolean isSuitableCapitalTile(Tile tile) {
        TerrainType terrainType =
            tile.getTerrainType();

        return terrainType == TerrainType.PLAIN
            || terrainType == TerrainType.FOREST;
    }

    private int scoreTile(Tile tile) {
        return tile.getResources()
            .get(ResourceType.FOOD) * 4
            + tile.getResources()
                .get(ResourceType.WOOD) * 2
            + tile.getResources()
                .get(ResourceType.STONE);
    }

    private static boolean isFarEnough(
        Position candidate,
        List<Position> selectedPositions
    ) {
        return selectedPositions.stream()
                .allMatch(position ->
                    manhattanDistance(
                        candidate,
                        position
                    ) >= MINIMUM_DISTANCE
                );
    }

    private static int manhattanDistance(
        Position first,
        Position second
    ) {
        return Math.abs(first.x() - second.x())
            + Math.abs(first.y() - second.y());
    }

    private Population generateInitialPopulation(
        Tile capitalTile
    ) {
        int foodPotential = capitalTile
            .getResources()
            .get(ResourceType.FOOD);

        int basePopulation = 700;
        int foodBonus = foodPotential * 150;
        int randomBonus = randomGenerator.nextInt(301);

        int inhabitants =
            basePopulation + foodBonus + randomBonus;

        double growthRate =
            0.01 + foodPotential * 0.005;

        return new Population(
            inhabitants,
            growthRate
        );
    }
}