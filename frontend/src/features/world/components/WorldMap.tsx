import type { CSSProperties } from "react";

import { TERRAIN_VISUALS } from "../config/terrain";
import { RESOURCE_LABELS } from "../config/resources";
import type { World } from "../types/world";

interface WorldMapProps {
    world: World;
}

export function WorldMap({ world }: WorldMapProps) {
    const gridStyle: CSSProperties = {
        gridTemplateColumns: `repeat(${world.width}, minmax(0, 1fr))`,
    };

    const civilizationsByPosition = new Map(
        world.civilizations.map((civilization) => [
           `${civilization.capital.position.x}-${civilization.capital.position.y}`,
           civilization
        ]),
    );

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

                const resourcesLabel = Object.entries(
                    tile.resources.values,
                )
                .map(([resourceType, quantity]) => {
                    const typedResourceType =
                        resourceType as keyof typeof RESOURCE_LABELS;

                    return `${RESOURCE_LABELS[typedResourceType]} : ${quantity}`;
                })
                .join(", ");

                const positionKey =
                    `${tile.position.x}-${tile.position.y}`;

                const civilization =
                    civilizationsByPosition.get(positionKey);

                return (
                    <div
                        key={positionKey}
                        className="world-tile"
                        style={{
                            backgroundColor: terrainVisual.color,
                        }}
                        role="gridcell"
                        title={[
                            `${terrainVisual.label} — (${tile.position.x}, ${tile.position.y})`,
                            resourcesLabel,
                            civilization
                                ? `${civilization.name} — capitale : ${civilization.capital.name}`
                                : "",
                        ]
                            .filter(Boolean)
                            .join("\n")}
                    >
                        {civilization && (
                            <span
                                className="capital-marker"
                                style={{
                                    backgroundColor: civilization.color,
                                }}
                                aria-label={`Capitale ${civilization.capital.name} des ${civilization.name}`}
                            />
                        )}
                    </div>
                )
            })}
        </div>
    );
}