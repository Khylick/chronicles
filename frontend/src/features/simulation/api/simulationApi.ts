import type { Simulation } from "../types/simulation";

const API_URL = import.meta.env.VITE_API_URL ?? "";

export async function createSimulation(
    width: number,
    height: number,
    signal?: AbortSignal,
): Promise<Simulation> {
    const response = await fetch(
        `${API_URL}/api/simulation?width=${width}&height=${height}`,
        {
            method: "POST",
            signal,
        },
    );

    if (!response.ok) {
        throw new Error(
            "Impossible de créer la simulation",
        );
    }

    return response.json();
}

export async function nextTurn(): Promise<Simulation> {
    const response = await fetch(
        `${API_URL}/api/simulation/next-turn`,
        {
            method: "POST",
            headers: {
                Accept: "application/json",
            },
        },
    );

    if (!response.ok) {
        throw new Error(
            `Impossible de passer au tour suivant (${response.status})`,
        );
    }

    return await response.json() as Promise<Simulation>;
}