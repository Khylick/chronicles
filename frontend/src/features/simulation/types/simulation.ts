import type {
    Civilization,
    ResourceType,
    World
} from "../../world/types/world";

export interface ResourceStock {
    values: Partial<Record<ResourceType, number>>;
}

export interface CivilizationState {
    civilizationId: string;
    civilization: Civilization;
    stock: ResourceStock;
}

export interface Simulation {
    turn: number;
    world: World;
    civilizationStates: CivilizationState[];
}