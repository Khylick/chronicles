package fr.khylick.chronicles.world.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class PopulationTest {

    @Test
    void shouldExposePopulationInformation() {
        Population population =
            new Population(1_250, 0.025);

        assertThat(population.getInhabitants())
            .isEqualTo(1_250);

        assertThat(population.getGrowthRate())
            .isEqualTo(0.025);
    }

    @Test
    void shouldCalculateFoodConsumptionPerTurn() {
        Population population =
            new Population(1_250, 0.025);

        assertThat(
            population.getFoodConsumptionPerTurn()
        ).isEqualTo(13);
    }

    @Test
    void shouldRoundFoodConsumptionUp() {
        Population population =
            new Population(101, 0.01);

        assertThat(
            population.getFoodConsumptionPerTurn()
        ).isEqualTo(2);
    }

    @Test
    void shouldRejectNonPositivePopulation() {
        assertThatThrownBy(
            () -> new Population(0, 0.02)
        )
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining(
                "strictement positive"
            );
    }

    @Test
    void shouldRejectNegativeGrowthRate() {
        assertThatThrownBy(
            () -> new Population(1_000, -0.01)
        )
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining(
                "ne peut pas être négatif"
            );
    }

    @Test
    void shouldGrowPopulation() {
        Population population =
            new Population(1_000, 0.03);

        Population grownPopulation =
            population.grow();

        assertThat(grownPopulation.getInhabitants())
            .isEqualTo(1_030);

        assertThat(grownPopulation.getGrowthRate())
            .isEqualTo(0.03);
    }

    @Test
    void shouldNotMutateOriginalPopulationWhenGrowing() {
        Population population =
            new Population(1_000, 0.03);

        population.grow();

        assertThat(population.getInhabitants())
            .isEqualTo(1_000);
    }
}