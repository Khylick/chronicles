package fr.khylick.chronicles.simulation.infrastructure;

import fr.khylick.chronicles.simulation.application.*;
import fr.khylick.chronicles.world.application.DefaultTerritoryProductionCalculator;
import fr.khylick.chronicles.world.application.TerritoryProductionCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SimulationConfiguration {

    @Bean
    TerritoryExpansionService territoryExpansionService() {
        return new DefaultTerritoryExpansionService();
    }

    @Bean
    TerritoryProductionCalculator territoryProductionCalculator() {
        return new DefaultTerritoryProductionCalculator();
    }

    @Bean
    SimulationEngine simulationEngine(
        TerritoryProductionCalculator
            territoryProductionCalculator,
        TerritoryExpansionService
            territoryExpansionService
    ) {
        return new DefaultSimulationEngine(
            territoryProductionCalculator,
            territoryExpansionService
        );
    }

    @Bean
    SimulationFactory simulationFactory() {
        return new DefaultSimulationFactory();
    }
}