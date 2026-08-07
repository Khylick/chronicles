package fr.khylick.chronicles.simulation.infrastructure;

import fr.khylick.chronicles.simulation.application.SimulationFactory;
import fr.khylick.chronicles.world.application.WorldGenerator;
import fr.khylick.chronicles.world.domain.World;
import org.springframework.web.bind.annotation.*;

import fr.khylick.chronicles.simulation.application.SimulationEngine;
import fr.khylick.chronicles.simulation.domain.Simulation;

@RestController
@RequestMapping("/api/simulation")
public class SimulationController {

    private final WorldGenerator worldGenerator;
    private final SimulationFactory simulationFactory;
    private final SimulationEngine simulationEngine;

    private Simulation currentSimulation;

    public SimulationController(
        WorldGenerator worldGenerator,
        SimulationFactory simulationFactory,
        SimulationEngine simulationEngine
    ) {
        this.worldGenerator = worldGenerator;
        this.simulationFactory = simulationFactory;
        this.simulationEngine = simulationEngine;
    }

    @PostMapping
    public Simulation createSimulation(
        @RequestParam(defaultValue = "80")
        int width,

        @RequestParam(defaultValue = "48")
        int height
    ) {
        World world =
            worldGenerator.generate(
                width,
                height
            );

        currentSimulation =
            simulationFactory.create(world);

        return currentSimulation;
    }

    @PostMapping("/next-turn")
    public Simulation nextTurn() {
        if (currentSimulation == null) {
            throw new IllegalStateException(
                "Aucune simulation active"
            );
        }

        currentSimulation =
            simulationEngine.nextTurn(
                currentSimulation
            );

        return currentSimulation;
    }
}