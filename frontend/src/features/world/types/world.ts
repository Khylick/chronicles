export type TerrainType =
    | "OCEAN"
    | "BEACH"
    | "PLAIN"
    | "FOREST"
    | "HILL"
    | "MOUNTAIN";

export interface Position {
    x: number;
    y: number;
}

export interface Tile {
    position: Position;
    terrainType: TerrainType;
    resources: TileResources;
}

export interface World {
    width: number;
    height: number;
    tiles: Tile[];
    civilizations: Civilization[];
    territories: Territory[];
}

export type ResourceType =
    | "FOOD"
    | "WOOD"
    | "STONE"
    | "ORE";

export interface TileResources {
    values: Partial<Record<ResourceType, number>>;
}

export interface Capital {
    id: string;
    name: string;
    position: Position;
}

export interface Civilization {
    id: string;
    name: string;
    color: string;
    capital: Capital;
}

export interface Territory {
    civilizationId: string;
    positions: Position[];
}