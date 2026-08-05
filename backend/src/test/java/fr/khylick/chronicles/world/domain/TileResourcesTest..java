package fr.khylick.chronicles.world.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;

import org.junit.jupiter.api.Test;

class TileResourcesTest {

    @Test
    void shouldReturnConfiguredResourceQuantity() {
        TileResources resources =
            TileResources.builder()
                .add(ResourceType.FOOD, 4)
                .add(ResourceType.WOOD, 2)
                .build();

        assertThat(resources.get(ResourceType.FOOD))
            .isEqualTo(4);

        assertThat(resources.get(ResourceType.WOOD))
            .isEqualTo(2);
    }

    @Test
    void shouldReturnZeroForMissingResource() {
        TileResources resources =
            TileResources.of(
                ResourceType.FOOD,
                4
            );

        assertThat(resources.get(ResourceType.ORE))
            .isZero();
    }

    @Test
    void shouldRejectNegativeQuantity() {
        assertThatThrownBy(() ->
            new TileResources(
                Map.of(ResourceType.FOOD, -1)
            )
        )
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("négative");
    }

    @Test
    void shouldIgnoreZeroQuantities() {
        TileResources resources =
            TileResources.builder()
                .add(ResourceType.FOOD, 0)
                .build();

        assertThat(resources.isEmpty()).isTrue();
    }
}