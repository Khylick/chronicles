import {
    useCallback,
    useEffect,
    useState,
} from "react";

import { createSimulation, nextTurn } from "../../simulation/api/simulationApi";
import type { Simulation } from "../../simulation/types/simulation";

interface UseSimulationOptions {
    width: number;
    height: number;
}

interface UseSimulationResult {
    simulation: Simulation | null;
    error: string | null;
    generateSimulation: () => Promise<void>;
    advanceTurn: () => Promise<void>;
}

export function useSimulation({
                                  width,
                                  height,
                              }: UseSimulationOptions): UseSimulationResult {
    const [simulation, setSimulation] =
        useState<Simulation | null>(null);

    const [error, setError] =
        useState<string | null>(null);

    /*
     * Chargement automatique de la simulation
     * lors du montage du composant ou si les
     * dimensions changent.
     */
    useEffect(() => {
        let cancelled = false;

        createSimulation(width, height)
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
            });

        return () => {
            cancelled = true;
        };
    }, [width, height]);

    /*
     * Action appelée manuellement par le bouton
     * "Générer un nouveau monde".
     */
    const generateSimulation =
        useCallback(async () => {
            try {
                const newSimulation =
                    await createSimulation(
                        width,
                        height,
                    );

                setSimulation(newSimulation);
                setError(null);
            } catch (requestError: unknown) {
                const message =
                    requestError instanceof Error
                        ? requestError.message
                        : "Une erreur inconnue est survenue";

                setError(message);
            }
        }, [width, height]);

    /*
     * Action appelée par le bouton
     * "Passer un tour".
     */
    const advanceTurn =
        useCallback(async () => {
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
        }, [simulation]);

    return {
        simulation,
        error,
        generateSimulation,
        advanceTurn,
    };
}