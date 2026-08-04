import { TERRAIN_VISUALS } from "../config/terrain";
import type {
    TerrainType,
    World,
} from "../types/world";

interface TerrainLegendProps {
    world: World;
}

const TERRAIN_TYPES = Object.keys(
    TERRAIN_VISUALS,
) as TerrainType[];

export function TerrainLegend({
    world,
}: TerrainLegendProps) {
    const terrainCounts = world.tiles.reduce<
        Record<TerrainType, number>
    >(
        (counts, tile) => {
            counts[tile.terrainType] += 1;
            return counts;
        },
        {
            OCEAN: 0,
            BEACH: 0,
            PLAIN: 0,
            FOREST: 0,
            HILL: 0,
            MOUNTAIN: 0,
        },
    );

    return (
        <ul
            className="terrain-legend"
            aria-label="Légende des terrains"
        >
            {TERRAIN_TYPES.map((terrainType) => {
                const terrainVisual =
                    TERRAIN_VISUALS[terrainType];

                return (
                    <li key={terrainType}>
                        <span
                            className="terrain-legend-color"
                            style={{
                                backgroundColor: terrainVisual.color,
                            }}
                            aria-hidden="true"
                        />

                        <span>
                            {terrainVisual.label}{" "}
                            <strong>
                                ({terrainCounts[terrainType]})
                            </strong>
                        </span>
                    </li>
                );
            })}
        </ul>
    );
}