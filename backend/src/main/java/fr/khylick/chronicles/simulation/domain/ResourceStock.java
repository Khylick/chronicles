package fr.khylick.chronicles.simulation.domain;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import fr.khylick.chronicles.world.domain.ResourceType;

public final class ResourceStock {

    private final Map<ResourceType, Integer> values;

    public ResourceStock() {
        this(Map.of());
    }

    public ResourceStock(
        Map<ResourceType, Integer> values
    ) {
        Objects.requireNonNull(
            values,
            "Les stocks sont obligatoires"
        );

        EnumMap<ResourceType, Integer> copy =
            new EnumMap<>(ResourceType.class);

        values.forEach((resourceType, quantity) -> {
          Objects.requireNonNull(resourceType);
          Objects.requireNonNull(quantity);

          if (quantity < 0) {
              throw new IllegalArgumentException(
                  "Un stock ne peut pas être négatif"
              );
          }

          if (quantity > 0) {
              copy.put(resourceType, quantity);
          }
        });

        this.values =
            Collections.unmodifiableMap(copy);
    }

    public Map<ResourceType, Integer> getValues() {
        return values;
    }

    public int get(ResourceType resourceType) {
        return values.getOrDefault(resourceType, 0);
    }

    public ResourceStock add(
        ResourceType resourceType,
        int quantity
    ) {
        if (quantity < 0) {
            throw new IllegalArgumentException(
                "La quantité ajoutée ne peut pas être négative"
            );
        }

        EnumMap<ResourceType, Integer> updated =
            new EnumMap<>(ResourceType.class);

        updated.putAll(values);

        updated.merge(
            resourceType,
            quantity,
            Integer::sum
        );

        return new ResourceStock(updated);
    }

    public ResourceStock consume(
        ResourceType resourceType,
        int quantity
    ) {
        if (quantity < 0) {
            throw new IllegalStateException(
                "La quantité consommée ne peut pas être négative"
            );
        }

        int current = get(resourceType);

        if (current < quantity) {
            throw new IllegalStateException(
                "Stock insuffisant pour " + resourceType
            );
        }

        EnumMap<ResourceType, Integer> updated =
            new EnumMap<>(ResourceType.class);

        updated.putAll(values);

        int remaining = current - quantity;

        if (remaining == 0) {
            updated.remove(resourceType);
        } else {
            updated.put(resourceType, remaining);
        }

        return new ResourceStock(updated);
    }
}