package bomberman.managers;

import bomberman.powerups.PowerUpType;
import static bomberman.GameConstants.*;

public class StatusManager {
    private int lives = PLAYER_START_LIVES;
    private int bombPower = BOMB_BASE_POWER;
    private int speedBoosts = 0;

    public void applyPowerUpEffect(PowerUpType type) {
        switch (type) {
            case BOMB_POWER_UP:
                setBombPower(bombPower + 1);
                break;
            case SPEED_UP:
                increaseSpeedBoost();
                break;
            case HEALTH_UP:
                gainLife();
                break;
            case BOMB_POWER_DOWN:
                setBombPower(bombPower - 1);
                break;
            case SPEED_DOWN:
                decreaseSpeedBoost();
                break;
            case HEALTH_DOWN:
                loseLife();
                break;
        }
    }

    public void reset() {
        lives = PLAYER_START_LIVES;
        bombPower = BOMB_BASE_POWER;
        speedBoosts = 0;
    }

    public void loseLife() {
        lives = Math.max(lives - 1, 0);
    }

    public void gainLife() {
        lives = Math.min(lives + 1, PLAYER_MAX_LIVES);
    }

    private void setBombPower(int power) {
        bombPower = Math.min(Math.max(power, 1), MAX_BOMB_POWER);
    }

    private void increaseSpeedBoost() {
        speedBoosts = Math.min(speedBoosts + 1, MAX_SPEED_BOOSTS);
    }

    private void decreaseSpeedBoost() {
        speedBoosts = Math.max(speedBoosts - 1, -MAX_SPEED_BOOSTS);
    }

    // Getters
    public int getLives() { return lives; }
    public int getBombPower() { return bombPower; }
    public float getSpeedMultiplier() { return 1.0f + (0.1f * speedBoosts); }
    public boolean isAlive() { return lives > 0; }
}