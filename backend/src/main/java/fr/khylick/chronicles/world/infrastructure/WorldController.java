package fr.khylick.chronicles.world.infrastructure;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.khylick.chronicles.world.application.WorldGenerator;
import fr.khylick.chronicles.world.domain.World;

@RestController
@RequestMapping("/api/world")
public class WorldController {

    private static final int MAX_WIDTH = 200;
    private static final int MAX_HEIGHT = 200;

    private final WorldGenerator worldGenerator;

    public WorldController(WorldGenerator worldGenerator) {
        this.worldGenerator = worldGenerator;
    }

    @GetMapping
    public ResponseEntity<World> generateWorld(
        @RequestParam(defaultValue = "50") int width,
        @RequestParam(defaultValue = "30") int height
    ) {
        validateDimensions(width, height);

        return ResponseEntity.ok(
            worldGenerator.generate(width, height)
        );
    }

    private void validateDimensions(int width, int height) {
        if (width > MAX_WIDTH) {
            throw new IllegalArgumentException(
                "La largeur maximale autorisée est de " + MAX_WIDTH
            );
        }

        if (height > MAX_HEIGHT) {
            throw new IllegalArgumentException(
                "LA hauteur maximale autorisée est de " + MAX_HEIGHT
            );
        }
    }
}