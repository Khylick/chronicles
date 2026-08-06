package fr.khylick.chronicles.world.application;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

import fr.khylick.chronicles.world.domain.Civilization;
import fr.khylick.chronicles.world.domain.Position;
import fr.khylick.chronicles.world.domain.TerrainType;
import fr.khylick.chronicles.world.domain.Territory;
import fr.khylick.chronicles.world.domain.Tile;
import fr.khylick.chronicles.world.domain.World;

public class InitialTerritoryGenerator
    implements TerritoryGenerator {

    private static final int[][] DIRECTIONS = {
        {0, -1},
        {1, 0},
        {0, 1},
        {-1, 0}
    };

    @Override
    public List<Territory> generate(
        World world,
        List<Civilization> civilizations,
        int maximumDistance
    ) {
        if (maximumDistance < 0) {
            throw new IllegalArgumentException(
                "La distance maximale ne peut pas être négative"
            );
        }

        if (civilizations.isEmpty()) {
            return List.of();
        }

        Map<UUID, Set<Position>> positionsByCivilization =
            new LinkedHashMap<>();

        Map<Position, UUID> claimedPositions =
            new HashMap<>();

        Queue<ExpansionNode> queue =
            new ArrayDeque<>();

        for (Civilization civilization : civilizations) {
            Position capitalPosition =
                civilization
                    .getCapital()
                    .getPosition();

            positionsByCivilization.put(
                civilization.getId(),
                new LinkedHashSet<>()
            );

            claimPosition(
                civilization.getId(),
                capitalPosition,
                positionsByCivilization,
                claimedPositions
            );

            queue.add(
                new ExpansionNode(
                    civilization.getId(),
                    capitalPosition,
                    0
                )
            );
        }

        while (!queue.isEmpty()) {
            ExpansionNode current = queue.remove();

            if (current.distance() >= maximumDistance) {
                continue;
            }

            for (int[] direction : DIRECTIONS) {
                int neighbourX =
                    current.position().x() + direction[0];

                int neighbourY =
                    current.position().y() + direction[1];

                if (
                    neighbourX < 0
                        || neighbourX >= world.getWidth()
                        || neighbourY < 0
                        || neighbourY >= world.getHeight()
                ) {
                    continue;
                }

                Position neighbour =
                    new Position(neighbourX, neighbourY);

                if (claimedPositions.containsKey(neighbour)) {
                    continue;
                }

                Tile tile = world.getTile(
                    neighbour.x(),
                    neighbour.y()
                );

                if (!isClaimable(tile)) {
                    continue;
                }

                claimPosition(
                    current.civilizationId(),
                    neighbour,
                    positionsByCivilization,
                    claimedPositions
                );

                queue.add(
                    new ExpansionNode(
                        current.civilizationId(),
                        neighbour,
                        current.distance() + 1
                    )
                );
            }
        }

        List<Territory> territories =
            new ArrayList<>();

        positionsByCivilization.forEach(
            (civilizationId, positions) ->
                territories.add(
                    new Territory(
                        civilizationId,
                        positions
                    )
                )
        );

        return List.copyOf(territories);
    }

    private static void claimPosition(
        UUID civilizationId,
        Position position,
        Map<UUID, Set<Position>> positionsByCivilization,
        Map<Position, UUID> claimedPositions
    ) {
        claimedPositions.put(
            position,
            civilizationId
        );

        positionsByCivilization
            .get(civilizationId)
            .add(position);
    }

    private static boolean isClaimable(Tile tile) {
        return switch (tile.getTerrainType()) {
            case OCEAN, MOUNTAIN -> false;
            case BEACH, PLAIN, FOREST, HILL -> true;
        };
    }

    private record ExpansionNode(
        UUID civilizationId,
        Position position,
        int distance
    ){
    }
}