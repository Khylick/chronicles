package fr.khylick.chronicles.simulation.application;

import fr.khylick.chronicles.simulation.domain.Simulation;
import fr.khylick.chronicles.world.domain.World;

public interface SimulationFactory {

    Simulation create(World world);
}