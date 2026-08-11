package fr.khylick.chronicles.world.domain;

import java.util.Objects;
import java.util.UUID;

public final class Civilization {

    private final UUID id;
    private final String name;
    private final String color;
    private final Capital capital;
    private final Population population;

    public Civilization(
        UUID id,
        String name,
        String color,
        Capital capital,
        Population population
    ) {
        this.id = Objects.requireNonNull(
            id,
            "L'identifiant de la civilisation est obligatoire"
        );

        this.name = requireNonBlank(
            name,
            "Le nom de la civilisation est obligatoire"
        );

        this.color = requireNonBlank(
            color,
            "La couleur de la civilisation est obligatoire"
        );

        this.capital = Objects.requireNonNull(
            capital,
            "La capitale de la civilisation est obligatoire"
        );

        this.population = Objects.requireNonNull(
            population,
            "La population de la civilisation est obligatoire"
        );
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Capital getCapital() {
        return capital;
    }

    public Population getPopulation() {
        return population;
    }

    public Civilization withPopulation(
        Population newPopulation
    ) {
        return new Civilization(
            id,
            name,
            color,
            capital,
            newPopulation
        );
    }

    private static String requireNonBlank(
        String value,
        String message
    ) {
        Objects.requireNonNull(value, message);

        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }

        return value;
    }
}