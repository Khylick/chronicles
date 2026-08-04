import type { TerrainType } from "../types/world";

interface TerrainVisual {
    label: string;
    color: string;
}

export const TERRAIN_VISUALS: Record<
    TerrainType,
    TerrainVisual
> = {
    OCEAN: {
        label: "Océan",
        color: "#2474a6"
    },
    BEACH: {
        label: "Plage",
        color: "#e9d8a6",
    },
    PLAIN: {
        label: "Plaine",
        color: "#8fbc67",
    },
    FOREST: {
        label: "Forêt",
        color: "#3F7d44",
    },
    HILL: {
        label: "Colline",
        color: "#9a7b4f",
    },
    MOUNTAIN: {
        label: "Montagne",
        color: "#8a8f98",
    },
};