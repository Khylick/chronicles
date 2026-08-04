import type { World } from "../features/world/types/world";

const API_URL = import.meta.env.VITE_API_URL ?? "";

export interface GenerateWorldParameters {
    width: number;
    height: number;
}

export async function generateWorld(
    parameters: GenerateWorldParameters,
    signal?: AbortSignal
): Promise<World> {
    const searchParameters = new URLSearchParams({
        width: parameters.width.toString(),
        height: parameters.height.toString(),
    });

    const response = await fetch(
        `${API_URL}/api/world?${searchParameters.toString()}`,
        {
            method: "GET",
            signal,
            headers: {
                Accept: "application/json",
            },
        },
    );

    if (!response.ok) {
        throw new Error(
            await extractErrorMessage(response),
        );
    }

    return await response.json() as Promise<World>;
}

async function extractErrorMessage(
    response: Response
): Promise<string> {
   try {
       const body = (await response.json()) as {
           message?: string;
       };

       return body.message ?? `Erreur HTTP ${response.status}`;
   } catch {
       return `Erreur HTTP ${response.status}`;
   }
}