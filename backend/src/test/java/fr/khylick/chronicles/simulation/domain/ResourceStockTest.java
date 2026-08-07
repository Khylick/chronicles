package fr.khylick.chronicles.simulation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import fr.khylick.chronicles.world.domain.ResourceType;

class ResourceStockTest {

    @Test
    void shouldStartEmpty() {
        ResourceStock stock = new ResourceStock();

        assertThat(stock.get(ResourceType.FOOD))
                .isZero();
    }

    @Test
    void shouldAddResources() {
        ResourceStock stock =
                new ResourceStock()
                        .add(ResourceType.FOOD, 50)
                        .add(ResourceType.FOOD, 20);

        assertThat(stock.get(ResourceType.FOOD))
                .isEqualTo(70);
    }

    @Test
    void shouldConsumeResources() {
        ResourceStock stock =
                new ResourceStock()
                        .add(ResourceType.FOOD, 50)
                        .consume(ResourceType.FOOD, 15);

        assertThat(stock.get(ResourceType.FOOD))
                .isEqualTo(35);
    }

    @Test
    void shouldRejectConsumptionAboveAvailableStock() {
        ResourceStock stock =
            new ResourceStock()
                .add(ResourceType.FOOD, 10);

        assertThatThrownBy(
            () -> stock.consume(ResourceType.FOOD, 11)
        )
        .isInstanceOf(IllegalStateException.class);
    }
}