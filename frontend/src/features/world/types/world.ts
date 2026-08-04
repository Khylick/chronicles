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
}

export interface World {
    width: number;
    height: number;
    tiles: Tile[];
}