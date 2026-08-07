import { useEffect, useState } from "react";

import type { World } from "../types/world";
import { createSimulation, nextTurn } from "../../simulation/api/simulationApi";
import type { Simulation } from "../../simulation/types/simulation";

interface UseSimulationResult {
    simulation: Simulation | null;
    isLoading: boolean;
    error: string | null;
    advanceTurn: () => Promise<void>;
}

const SIMULATION_WIDTH: number = 80;
const SIMULATION_HEIGHT: number = 48;

export function useSimulation(
    world: World | null,
): UseSimulationResult {
    const [simulation, setSimulation] =
        useState<Simulation | null>(null);

    const [isLoading, setIsLoading] =
        useState(false);

    const [error, setError] =
        useState<string | null>(null);

    useEffect(() => {
        if (!world) {
            return;
        }

        let cancelled = false;

        createSimulation(SIMULATION_WIDTH, SIMULATION_HEIGHT)
            .then((newSimulation) => {
                if (cancelled) {
                    return;
                }

                setSimulation(newSimulation);
                setError(null);
            })
            .catch((requestError: unknown) => {
                if (cancelled) {
                    return;
                }

                const message =
                    requestError instanceof Error
                        ? requestError.message
                        : "Une erreur inconnue est survenue";

                setError(message);
            })
            .finally(() => {
                if (!cancelled) {
                    setIsLoading(false);
                }
            });

        return () => {
            cancelled = true;
        };
    }, [world]);

    const advanceTurn = async () => {
        if (!simulation) {
            return;
        }

        try {
            const updatedSimulation =
                await nextTurn();

            setSimulation(updatedSimulation);
            setError(null);
        } catch (requestError: unknown) {
            const message =
                requestError instanceof Error
                    ? requestError.message
                    : "Une erreur inconnue est survenue";

            setError(message);
        }
    };

    return {
        simulation,
        isLoading,
        error,
        advanceTurn,
    };
}