package fr.khylick.chronicles.simulation.domain;

import java.util.Objects;
import java.util.UUID;

import fr.khylick.chronicles.world.domain.Civilization;

public final class CivilizationState {

    private final UUID civilizationId;
    private final Civilization civilization;
    private final ResourceStock stock;

    public CivilizationState(
        Civilization civilization,
        ResourceStock stock
    ) {
        this.civilization =
            Objects.requireNonNull(civilization);

        this.civilizationId = civilization.getId();

        this.stock =
            Objects.requireNonNull(stock);
    }

    public UUID getCivilizationId() {
        return civilizationId;
    }

    public Civilization getCivilization() {
        return civilization;
    }

    public ResourceStock getStock() {
        return stock;
    }
}