package fr.khylick.chronicles.simulation.domain;

import java.util.List;
import java.util.Objects;

import fr.khylick.chronicles.world.domain.Territory;
import fr.khylick.chronicles.world.domain.World;

public final class Simulation {

    private final int turn;
    private final World world;
    private final List<CivilizationState> civilizationStates;
    private final List<Territory> territories;

    public Simulation(
        int turn,
        World world,
        List<CivilizationState> civilizationStates,
        List<Territory> territories
    ) {
        if (turn < 0) {
            throw new IllegalArgumentException(
                "Le numéro du tour ne peut pas être négatif"
            );
        }

        this.turn = turn;

        this.world =
            Objects.requireNonNull(world);

        this.civilizationStates =
            List.copyOf(
                Objects.requireNonNull(
                    civilizationStates
                )
            );

        this.territories =
            List.copyOf(
                Objects.requireNonNull(
                    territories,
                    "Les territoires sont obligatoires"
                )
            );
    }

    public int getTurn() {
        return turn;
    }

    public World getWorld() {
        return world;
    }

    public List<CivilizationState> getCivilizationStates() {
        return civilizationStates;
    }

    public List<Territory> getTerritories() {
        return territories;
    }
}