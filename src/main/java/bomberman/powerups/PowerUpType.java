package bomberman.powerups;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public enum PowerUpType {
    BOMB_POWER_UP(true),
    SPEED_UP(true),
    HEALTH_UP(true),
    BOMB_POWER_DOWN(false),
    SPEED_DOWN(false),
    HEALTH_DOWN(false);

    private final boolean positive;
    private static final Random random = new Random();
    private static final List<PowerUpType> POSITIVE_TYPES = Arrays.asList(
            BOMB_POWER_UP, SPEED_UP, HEALTH_UP
    );
    private static final List<PowerUpType> NEGATIVE_TYPES = Arrays.asList(
            BOMB_POWER_DOWN, SPEED_DOWN, HEALTH_DOWN
    );

    PowerUpType(boolean positive) {
        this.positive = positive;
    }

    public boolean isPositive() {
        return positive;
    }

    public static PowerUpType getRandomPositive() {
        return POSITIVE_TYPES.get(random.nextInt(POSITIVE_TYPES.size()));
    }

    public static PowerUpType getRandomNegative() {
        return NEGATIVE_TYPES.get(random.nextInt(NEGATIVE_TYPES.size()));
    }
}