package fr.khylick.chronicles.simulation.application;

import fr.khylick.chronicles.simulation.domain.Simulation;

public interface SimulationEngine {

    Simulation nextTurn(Simulation simulation);
}