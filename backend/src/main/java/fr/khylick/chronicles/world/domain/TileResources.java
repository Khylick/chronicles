package fr.khylick.chronicles.world.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public final class TileResources {

    private final Map<ResourceType, Integer> values;

    public TileResources(Map<ResourceType, Integer> values) {
        Objects.requireNonNull(
            values,
            "Les ressources d'une case sont obligatoires"
        );

        EnumMap<ResourceType, Integer> validateValues =
            new EnumMap<>(ResourceType.class);

        values.forEach((resourceType, quantity) -> {
            Objects.requireNonNull(
                resourceType,
                "Le type de ressource est obligatoire"
            );

            Objects.requireNonNull(
                quantity,
                "La quantité d'une ressource est obligatoire"
            );

            if (quantity < 0) {
                throw new IllegalArgumentException(
                    "La quantité d'une ressource ne peut pas être négative"
                );
            }

            if (quantity > 0) {
                validateValues.put(resourceType, quantity);
            }
        });

        this.values = Collections.unmodifiableMap(
            validateValues
        );
    }

    public static TileResources empty() {
        return new TileResources(Map.of());
    }

    public static TileResources of(
        ResourceType resourceTypes,
        int quantity
    ) {
        return new TileResources(
            Map.of(resourceTypes, quantity)
        );
    }

    public int get(ResourceType resourceType) {
        Objects.requireNonNull(
            resourceType,
            "Le type de ressource est obligatoire"
        );

        return values.getOrDefault(resourceType, 0);
    }

    public Map<ResourceType, Integer> getValues() {
        return values;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return values.isEmpty();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final EnumMap<ResourceType, Integer> values =
                new EnumMap<>(ResourceType.class);

        private Builder() {
        }

        public Builder add(
                ResourceType resourceType,
                int quantity
        ) {
            Objects.requireNonNull(
                    resourceType,
                    "Le type de ressource est obligatoire"
            );

            if (quantity < 0) {
                throw new IllegalArgumentException(
                        "La quantité d'une ressource ne peut pas être négative"
                );
            }

            if (quantity > 0) {
                values.put(resourceType, quantity);
            }

            return this;
        }

        public TileResources build() {
            return new TileResources(values);
        }
    }
}