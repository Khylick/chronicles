package fr.khylick.chronicles.world.domain;

public final class Population {

    private static final int INHABITANTS_PER_FOOD_UNIT = 100;

    private final int inhabitants;
    private final double growthRate;

    public Population(
        int inhabitants,
        double growthRate
    ) {
        if (inhabitants <= 0) {
            throw new IllegalArgumentException(
                "La population doit être strictement positive"
            );
        }

        if (growthRate < 0.0) {
            throw new IllegalArgumentException(
                "Le taux de croissance ne peut pas être négatif"
            );
        }

        this.inhabitants = inhabitants;
        this.growthRate = growthRate;
    }

    public int getInhabitants() {
        return inhabitants;
    }

    public double getGrowthRate() {
        return growthRate;
    }

    public int getFoodConsumptionPerTurn() {
        return (int) Math.ceil(
            inhabitants / (double) INHABITANTS_PER_FOOD_UNIT
        );
    }

    public Population grow() {
        int newInhabitants = (int) Math.floor(
            inhabitants * (1.0 + growthRate)
        );

        return new Population(
            Math.max(inhabitants + 1, newInhabitants),
            growthRate
        );
    }

    public Population decline(
        double declineRate
    ) {
        if (declineRate < 0.0) {
            throw new IllegalArgumentException(
                "Le taux de décroissance ne peut pas être négatif"
            );
        }

        int lostInhabitants = (int) Math.ceil(
            inhabitants * declineRate
        );

        int newInhabitants = Math.max(
            1,
            inhabitants - lostInhabitants
        );

        return new Population(
            newInhabitants,
            growthRate
        );
    }
}