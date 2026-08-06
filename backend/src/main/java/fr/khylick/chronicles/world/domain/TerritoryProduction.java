package fr.khylick.chronicles.world.domain;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class TerritoryProduction {

    private final UUID civilizationId;
    private final Map<ResourceType, Integer> values;

    public TerritoryProduction(
        UUID civilizationId,
        Map<ResourceType, Integer> values
    ) {
        this.civilizationId = Objects.requireNonNull(
            civilizationId,
            "L'identifiant de la civilisation est obligatoire"
        );

        Objects.requireNonNull(
            values,
            "Les valeurs de production sont obligatoires"
        );

        EnumMap<ResourceType, Integer> validatedValues =
            new EnumMap<>(ResourceType.class);

        values.forEach((resourceType, quantity) -> {
            Objects.requireNonNull(
                resourceType,
                "Le type de ressource est obligatoire"
            );

            Objects.requireNonNull(
                quantity,
                "La quantité produite est obligatoire"
            );

            if (quantity < 0) {
                throw new IllegalArgumentException(
                    "Une quantité produite ne peut pas être négative"
                );
            }

            if (quantity > 0) {
                validatedValues.put(resourceType, quantity);
            }
        });

        this.values = Collections.unmodifiableMap(
            validatedValues
        );
    }

    public UUID getCivilizationId() {
        return civilizationId;
    }

    public Map<ResourceType, Integer> getValues() {
        return values;
    }

    public int get(ResourceType resourceType) {
        Objects.requireNonNull(
            resourceType,
            "Le type de ressource est obligatoire"
        );

        return values.getOrDefault(resourceType, 0);
    }
}