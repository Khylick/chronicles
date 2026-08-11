package fr.khylick.chronicles.simulation.domain;

import java.util.Objects;
import java.util.UUID;

import fr.khylick.chronicles.world.domain.Population;

public final class CivilizationState {

    private final UUID civilizationId;
    private final Population population;
    private final ResourceStock stock;

    public CivilizationState(
        UUID civilizationId,
        Population population,
        ResourceStock stock
    ) {
        this.civilizationId =
            Objects.requireNonNull(
                civilizationId,
                "L'identifiant de la civilisation est obligatoire"
        );

        this.population =
            Objects.requireNonNull(
                population,
                "La population de la civilisation est obligatoire"
        );

        this.stock =
            Objects.requireNonNull(
                stock,
                "Le stock de ressources de la civilisation est obligatoire"
        );
    }

    public UUID getCivilizationId() {
        return civilizationId;
    }

    public Population getPopulation() {
        return population;
    }

    public ResourceStock getStock() {
        return stock;
    }
}