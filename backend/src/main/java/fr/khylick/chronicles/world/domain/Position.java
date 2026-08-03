package fr.khylick.chronicles.world.domain;

public record Position(int x, int y) {

    public Position {
        if(x < 0) {
            throw new IllegalArgumentException("La position x ne peut pas être négative.");
        }

        if(y < 0) {
            throw new IllegalArgumentException("La position y ne peut pas être négative.");
        }
    }
}