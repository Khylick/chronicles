package fr.khylick.chronicles.world.domain;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Territory {

    private final UUID civilizationId;
    private final Set<Position> positions;

    public Territory(
        UUID civilizationId,
        Set<Position> positions
    ) {
        this.civilizationId = Objects.requireNonNull(
            civilizationId,
            "L'identifiant de la civilisation est obligatoire"
        );

        Objects.requireNonNull(
            positions,
            "Les positions du territoire sont obligatoires"
        );

        if (positions.isEmpty()) {
            throw new IllegalArgumentException(
                "Un territoire doit contenir au moins une position"
            );
        }

        this.positions = Set.copyOf(positions);
    }

    public UUID getCivilizationId() {
        return civilizationId;
    }

    public Set<Position> getPositions() {
        return positions;
    }

    public boolean contains(Position position) {
        return positions.contains(position);
    }
}