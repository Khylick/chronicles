import type { CSSProperties } from "react";

import { TERRAIN_VISUALS } from "../config/terrain";
import { RESOURCE_LABELS } from "../config/resources";
import type {Civilization, World} from "../types/world";

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

    const civilizationById = new Map(
        world.civilizations.map((civilization) => [
            civilization.id,
            civilization
        ]),
    );

    const territoryByPosition = new Map<
        string,
        Civilization
    >();

    world.territories.forEach((territory) => {
        const civilization =
            civilizationById.get(
                territory.civilizationId
            );

        if (!civilization) {
            return;
        }

        territory.positions.forEach((position) => {
            territoryByPosition.set(
                `${position.x}-${position.y}`,
                civilization,
            );
        });
    });

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

                const territoryCivilization =
                    territoryByPosition.get(positionKey);

                const territoryProduction =
                    territoryCivilization
                        ? world.territoryProductions.find(
                            (production) =>
                                production.civilizationId === territoryCivilization.id,
                        )
                        : undefined;

                return (
                    <div
                        key={positionKey}
                        className={[
                            "world-tile",
                            territoryCivilization
                                ? "world-tile--claimed"
                                : "",
                        ]
                            .filter(Boolean)
                            .join(" ")}
                        style={
                            {
                                backgroundColor: terrainVisual.color,
                                "--territory-color":
                                territoryCivilization?.color,
                            } as CSSProperties
                        }
                        role="gridcell"
                        title={[
                            `${terrainVisual.label} — (${tile.position.x}, ${tile.position.y})`,
                            resourcesLabel,
                            civilization
                                ? [
                                    `${civilization.name} — capitale : ${civilization.capital.name}`,
                                    `Population : ${civilization.capital.population.inhabitants.toLocaleString("fr-FR")}`,
                                    `Consommation : ${civilization.capital.population.foodConsumptionPerTurn} nourriture / tour`,
                                ].join("\n")
                                : "",
                            territoryProduction
                                ? `Production territoriale : nourriture ${
                                        territoryProduction.values.FOOD ?? 0
                                    }, bois ${
                                        territoryProduction.values.WOOD ?? 0
                                    }, pierre ${
                                        territoryProduction.values.STONE ?? 0
                                    }, minerai ${
                                        territoryProduction.values.ORE ?? 0
                                    }`
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