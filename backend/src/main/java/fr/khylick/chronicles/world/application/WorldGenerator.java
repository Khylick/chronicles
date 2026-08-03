package fr.khylick.chronicles.world.application;

import fr.khylick.chronicles.world.domain.World;

public interface WorldGenerator {

    World generate(int width, int height);
}