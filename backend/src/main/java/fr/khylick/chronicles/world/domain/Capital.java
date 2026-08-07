package fr.khylick.chronicles.world.domain;

import java.util.Objects;
import java.util.UUID;

public final class Capital {

    private final UUID id;
    private final String name;
    private final Position position;
    private final Population population;

    public Capital(
        UUID id,
        String name,
        Position position,
        Population population
    ) {
        this.id = Objects.requireNonNull(
            id,
            "L'identifiant de la capitale est obligatoire"
        );

        this.name = requireNonBlank(
            name,
            "Le nom de la capitale est obligatoire"
        );

        this.position = Objects.requireNonNull(
            position,
            "La position de la capitale est obligatoire"
        );

        this.population = Objects.requireNonNull(
            population,
            "La population de la capitale est obligatoire"
        );
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Position getPosition() {
        return position;
    }

    public Population getPopulation() {
        return population;
    }

    private String requireNonBlank(
        String value,
        String message
    ) {
        Objects.requireNonNull(value, message);

        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }

        return value;
    }

    public Capital withPopulation(
        Population newPopulation
    ) {
        return new Capital(
            id,
            name,
            position,
            newPopulation
        );
    }
}