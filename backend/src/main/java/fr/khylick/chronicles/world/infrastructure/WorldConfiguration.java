package fr.khylick.chronicles.world.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.khylick.chronicles.world.application.RandomWorldGenerator;
import fr.khylick.chronicles.world.application.WorldGenerator;

@Configuration
public class WorldConfiguration {

    @Bean
    public WorldGenerator worldGenerator() {
        return new RandomWorldGenerator();
    }
}