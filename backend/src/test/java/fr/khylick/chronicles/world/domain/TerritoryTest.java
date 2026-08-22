package fr.khylick.chronicles.world.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import java.util.UUID;

class TerritoryTest {
    @Test
    void shouldCreateNewTerritoryWithAdditionalPosition() {
        UUID civilizationId =
            UUID.randomUUID();

        Position initialPosition =
            new Position(3, 3);

        Position newPosition =
            new Position(4, 3);

        Territory territory =
            new Territory(
                civilizationId,
                Set.of(initialPosition)
            );

        Territory expanded =
            territory.withAdditionalPosition(
                newPosition
            );

        assertThat(territory.getPositions())
            .containsExactly(initialPosition);

        assertThat(expanded.getPositions())
            .contains(
                initialPosition,
                newPosition
            );
    }
}