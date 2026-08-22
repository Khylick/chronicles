import type {
    Population,
    ResourceType,
    Territory,
    World
} from "../../world/types/world";

export interface ResourceStock {
    values: Partial<Record<ResourceType, number>>;
}

export interface CivilizationState {
    civilizationId: string;
    population: Population;
    stock: ResourceStock;
}

export interface Simulation {
    turn: number;
    world: World;
    civilizationStates: CivilizationState[];
    territories: Territory[];
}