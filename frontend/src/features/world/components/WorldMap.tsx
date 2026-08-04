import type { CSSProperties } from "react";

import { TERRAIN_VISUALS } from "../config/terrain";
import type { World } from "../types/world";

interface WorldMapProps {
    world: World;
}

export function WorldMap({ world }: WorldMapProps) {
    const gridStyle: CSSProperties = {
        gridTemplateColumns: `repeat(${world.width}, minmax(0, 1fr))`,
    };

    return (
        <div
            className="world-map"
            style={gridStyle}
            role="grid"
            aria-label={`Carte du monde de ${world.width} colonnes et ${world.height} lignes`}
        >
            {world.tiles.map((tile) => {
                const terrainVisual =
                    TERRAIN_VISUALS[tile.terrainType];

                return (
                    <div
                        key={`${tile.position.x}-${tile.position.y}`}
                        className="world-tile"
                        style={{
                            backgroundColor: terrainVisual.color,
                        }}
                        role="gridcell"
                        title={`${terrainVisual.label} - (${tile.position.x}, ${tile.position.y})`}
                        aria-label={`${terrainVisual.label}, position ${tile.position.x}, ${tile.position.y}`}
                    />
                )
            })}
        </div>
    );
}