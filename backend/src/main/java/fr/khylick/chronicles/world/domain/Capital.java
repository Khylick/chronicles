package fr.khylick.chronicles.world.domain;

import java.util.Objects;
import java.util.UUID;

public final class Capital {

    private final UUID id;
    private final String name;
    private final Position position;

    public Capital(
        UUID id,
        String name,
        Position position
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
}