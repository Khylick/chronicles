package fr.khylick.chronicles.world.application;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import fr.khylick.chronicles.world.domain.ResourceType;
import fr.khylick.chronicles.world.domain.Territory;
import fr.khylick.chronicles.world.domain.TerritoryProduction;
import fr.khylick.chronicles.world.domain.World;

public class DefaultTerritoryProductionCalculator
    implements TerritoryProductionCalculator {

    @Override
    public List<TerritoryProduction> calculate(World world, List<Territory> territories) {
        List<TerritoryProduction> productions =
            new ArrayList<>();

        territories.forEach(territory -> {
            Map<ResourceType, Integer> totals =
                new EnumMap<>(ResourceType.class);

            territory.getPositions().forEach(position -> {
                var tile = world.getTile(
                    position.x(),
                    position.y()
                );

                tile.getResources()
                    .getValues()
                    .forEach((resourceType, quantity) ->
                        totals.merge(
                            resourceType,
                            quantity,
                            Integer::sum
                        )
                    );
            });

            productions.add(
                new TerritoryProduction(
                    territory.getCivilizationId(),
                    totals
                )
            );
        });

        return List.copyOf(productions);
    }
}