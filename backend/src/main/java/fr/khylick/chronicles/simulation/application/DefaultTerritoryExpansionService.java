package fr.khylick.chronicles.simulation.application;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import fr.khylick.chronicles.simulation.domain.CivilizationState;
import fr.khylick.chronicles.world.domain.Position;
import fr.khylick.chronicles.world.domain.ResourceType;
import fr.khylick.chronicles.world.domain.TerrainType;
import fr.khylick.chronicles.world.domain.Territory;
import fr.khylick.chronicles.world.domain.Tile;
import fr.khylick.chronicles.world.domain.World;

public final class DefaultTerritoryExpansionService
        implements TerritoryExpansionService {

    private static final int
        INHABITANTS_PER_TERRITORY_TILE = 50;

    private static final int[][] DIRECTIONS = {
        {0, -1},
        {1, 0},
        {0, 1},
        {-1, 0}
    };

    @Override
    public List<Territory> expand(
        World world,
        List<Territory> territories,
        List<CivilizationState> civilizationStates
    ) {
        List<Territory> updatedTerritories =
            new ArrayList<>(territories);

        Set<Position> claimedPositions =
            collectClaimedPositions(
                updatedTerritories
            );

        for (
            CivilizationState state :
            civilizationStates
        ) {
            int territoryIndex =
                findTerritoryIndex(
                    updatedTerritories,
                    state
                );

            Territory territory =
                updatedTerritories.get(
                    territoryIndex
                );

            if (!needsExpansion(
                state,
                territory
            )) {
                continue;
            }

            findBestCandidate(
                world,
                territory,
                claimedPositions
            ).ifPresent(position -> {
                Territory expandedTerritory =
                    territory
                        .withAdditionalPosition(
                            position
                        );

                updatedTerritories.set(
                    territoryIndex,
                    expandedTerritory
                );

                claimedPositions.add(
                    position
                );
            });
        }

        return List.copyOf(
            updatedTerritories
        );
    }

    private boolean needsExpansion(
        CivilizationState state,
        Territory territory
    ) {
        int population =
            state
                .getPopulation()
                .getInhabitants();

        int capacity =
            territory
                .getPositions()
                .size()
                * INHABITANTS_PER_TERRITORY_TILE;

        return population > capacity;
    }

    private java.util.Optional<Position>
    findBestCandidate(
        World world,
        Territory territory,
        Set<Position> claimedPositions
    ) {

        Set<Position> candidates =
            new LinkedHashSet<>();

        for (
            Position position :
            territory.getPositions()
        ) {
            for (int[] direction : DIRECTIONS) {
                int x =
                    position.x()
                        + direction[0];

                int y =
                    position.y()
                        + direction[1];

                if (
                    x < 0
                    || x >= world.getWidth()
                    || y < 0
                    || y >= world.getHeight()
                ) {
                    continue;
                }

                Position candidate =
                    new Position(x, y);

                if (
                    claimedPositions.contains(
                        candidate
                    )
                ) {
                    continue;
                }

                Tile tile =
                    world.getTile(x, y);

                if (!isClaimable(tile)) {
                    continue;
                }

                candidates.add(candidate);
            }
        }

        return candidates
            .stream()
            .max(
                Comparator.comparingInt(
                    position ->
                        scorePosition(
                            world,
                            position
                        )
                )
            );
    }

    private int scorePosition(
        World world,
        Position position
    ) {
        Tile tile =
            world.getTile(
                position.x(),
                position.y()
            );

        return tile
            .getResources()
            .get(ResourceType.FOOD)
            * 4
                + tile
                    .getResources()
                    .get(ResourceType.WOOD)
            * 2
                + tile
                    .getResources()
                    .get(ResourceType.STONE)
                + tile
                    .getResources()
                    .get(ResourceType.ORE);
    }

    private boolean isClaimable(
        Tile tile
    ) {
        TerrainType terrain =
            tile.getTerrainType();

        return terrain
            != TerrainType.OCEAN
            && terrain
            != TerrainType.MOUNTAIN;
    }

    private Set<Position>
        collectClaimedPositions(
            List<Territory> territories
    ) {

        Set<Position> claimed =
            new HashSet<>();

        territories.forEach(
            territory ->
                claimed.addAll(
                    territory.getPositions()
                )
        );

        return claimed;
    }

    private int findTerritoryIndex(
        List<Territory> territories,
        CivilizationState state
    ) {
        for (
            int index = 0;
            index < territories.size();
            index++
        ) {
            if (
                territories
                    .get(index)
                    .getCivilizationId()
                    .equals(
                        state.getCivilizationId()
                    )
            ) {
                return index;
            }
        }

        throw new IllegalStateException(
            "Territoire introuvable pour "
                + state.getCivilizationId()
        );
    }
}