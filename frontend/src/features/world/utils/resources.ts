import type {
    ResourceType,
    TerritoryProduction
} from "../types/world.ts";

export function getProductionQuantity(
    production: TerritoryProduction | undefined,
    resourceType: ResourceType,
): number {
    return production?.values[resourceType] ?? 0;
}