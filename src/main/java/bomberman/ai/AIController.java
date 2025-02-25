package bomberman.ai;

import bomberman.entities.Enemy;
import bomberman.entities.Player;
import java.awt.Point;
import java.util.List;

public class AIController {
    private final Enemy enemy;
    private final List<Enemy> allEnemies;
    private final Pathfinder pathfinder;

    public AIController(Enemy enemy, List<Enemy> allEnemies) {
        this.enemy = enemy;
        this.allEnemies = allEnemies;
        this.pathfinder = new Pathfinder(allEnemies);
    }

    public void update(Player player) {
        if (player == null) return;

        Point current = new Point(enemy.getXTile(), enemy.getYTile());
        Point target = new Point(player.getXTile(), player.getYTile());

        List<Point> path = pathfinder.findPath(current, target);

        if (!path.isEmpty()) {
            Point nextStep = path.get(0);
            int dx = nextStep.x - enemy.getXTile();
            int dy = nextStep.y - enemy.getYTile();
            enemy.move(dx, dy);
        }
    }
}