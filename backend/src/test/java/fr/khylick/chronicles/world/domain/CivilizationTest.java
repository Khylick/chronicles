package fr.khylick.chronicles.world.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

class CivilizationTest {

    @Test
    void shouldCreateNewCivilizationWhenPopulationChanges() {
        Capital capital =
            new Capital(
                UUID.randomUUID(),
                "Aldor",
                new Position(3, 2)
            );

        Civilization civilization =
            new Civilization(
                UUID.randomUUID(),
                "Aldéens",
                "#e63946",
                capital,
                new Population(1_000, 0.03)
            );

        Civilization updated =
            civilization.withPopulation(
                new Population(1_030, 0.03)
            );

        assertThat(
            civilization
                .getPopulation()
                .getInhabitants()
        ).isEqualTo(1_000);

        assertThat(
            updated
                .getPopulation()
                .getInhabitants()
        ).isEqualTo(1_030);

        assertThat(updated.getCapital())
            .isSameAs(capital);
    }
}