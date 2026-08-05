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
}

export type ResourceType =
    | "FOOD"
    | "WOOD"
    | "STONE"
    | "ORE";

export interface TileResources {
    values: Partial<Record<ResourceType, number>>;
}