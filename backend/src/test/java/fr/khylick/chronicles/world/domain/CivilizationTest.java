package fr.khylick.chronicles.world.domain;

import fr.khylick.chronicles.simulation.domain.CivilizationState;
import fr.khylick.chronicles.simulation.domain.ResourceStock;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

class CivilizationTest {

    @Test
    void shouldKeepCivilizationIdentityWhenPopulationChanges() {
        UUID civilizationId =
                UUID.randomUUID();

        ResourceStock resourceStock = new ResourceStock();

        Population initialPopulation =
                new Population(
                        1_000,
                        0.03
                );

        CivilizationState state =
                new CivilizationState(
                        civilizationId,
                        initialPopulation,
                        resourceStock
                );

        Population grownPopulation =
                state.getPopulation().grow();

        CivilizationState updated =
                new CivilizationState(
                        state.getCivilizationId(),
                        grownPopulation,
                        state.getStock()
                );

        assertThat(updated.getCivilizationId())
                .isEqualTo(civilizationId);

        assertThat(
                updated
                        .getPopulation()
                        .getInhabitants()
        ).isGreaterThan(
                initialPopulation.getInhabitants()
        );
    }
}