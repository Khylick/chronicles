package fr.khylick.chronicles.simulation.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.khylick.chronicles.simulation.application.DefaultSimulationEngine;
import fr.khylick.chronicles.simulation.application.DefaultSimulationFactory;
import fr.khylick.chronicles.simulation.application.SimulationEngine;
import fr.khylick.chronicles.simulation.application.SimulationFactory;

@Configuration
public class SimulationConfiguration {

    @Bean
    SimulationEngine simulationEngine() {
        return new DefaultSimulationEngine();
    }

    @Bean
    SimulationFactory simulationFactory() {
        return new DefaultSimulationFactory();
    }
}