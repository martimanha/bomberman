package bomberman.managers;

import bomberman.entities.Player;
import bomberman.powerups.PowerUp;
import java.util.Iterator;
import java.util.List;

public class PowerUpManager {
    private final List<PowerUp> powerUps;
    private final Player player;

    public PowerUpManager(List<PowerUp> powerUps, Player player) {
        this.powerUps = powerUps;
        this.player = player;
    }

    public void update() {
        processPowerUpCollisions();
        removeExpiredPowerUps();
    }

    private void processPowerUpCollisions() {
        Iterator<PowerUp> iterator = powerUps.iterator();
        while (iterator.hasNext()) {
            PowerUp powerUp = iterator.next();
            if (isPlayerColliding(powerUp)) {
                powerUp.applyEffect(player.getStatusManager());
                iterator.remove();
            }
        }
    }

    private boolean isPlayerColliding(PowerUp powerUp) {
        return player.getXTile() == powerUp.getXTile() &&
                player.getYTile() == powerUp.getYTile();
    }

    private void removeExpiredPowerUps() {
        powerUps.removeIf(powerUp -> !powerUp.isActive());
    }

    public void spawnPowerUp(PowerUp powerUp) {
        powerUps.add(powerUp);
    }
}