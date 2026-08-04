import { useCallback, useEffect, useState } from "react";

import { generateWorld } from "../../../api/worldApi";
import type { World } from "../types/world";

interface UseWorldResult {
    world: World | null;
    isLoading: boolean;
    error: string | null;
    regenerate: () => Promise<void>;
}

interface UseWorldParameters {
    width: number;
    height: number;
}

export function useWorld({
    width,
    height,
}: UseWorldParameters): UseWorldResult {
    const [world, setWorld] = useState<World | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const abortController = new AbortController();

        generateWorld(
            {
                width,
                height,
            },
            abortController.signal,
        )
        .then((generatedWorld) => {
            setWorld(generatedWorld);
            setError(null);
        })
        .catch((requestError: unknown) => {
            if (
                requestError instanceof DOMException &&
                requestError.name === "AbortError"
            ) {
                return;
            }

            const message =
                requestError instanceof Error
                ? requestError.message
                : "Une erreur inconnue est survenue";

                setError(message);
        })
        .finally(() => {
            if (!abortController.signal.aborted) {
                setIsLoading(false);
            }
        });

        return () => {
            abortController.abort();
        };
    }, [width, height]);

    const regenerate = useCallback(async () => {
        setIsLoading(true);
        setError(null);

        try {
            const generatedWorld = await generateWorld({
                width,
                height,
            });

            setWorld(generatedWorld);
        } catch (requestError: unknown) {
            const message =
                requestError instanceof Error
                    ? requestError.message
                    : "Une erreur inconnue est survenue";

            setError(message);
        } finally {
            setIsLoading(false);
        }
    }, [width, height]);

    return {
        world,
        isLoading,
        error,
        regenerate,
    };
}