package bomberman.ai;

import bomberman.managers.CollisionManager;
import bomberman.GameConstants;
import java.awt.Point;
import java.util.*;

public class Pathfinder {
    private static final int[][] DIRECTIONS = {{1,0}, {-1,0}, {0,1}, {0,-1}};

    public List<Point> findPath(Point start, Point target) {
        Queue<Node> queue = new LinkedList<>();
        Set<Point> visited = new HashSet<>();
        Map<Point, Point> cameFrom = new HashMap<>();

        Node startNode = new Node(start, 0);
        queue.add(startNode);
        visited.add(start);

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (current.position.equals(target)) {
                return reconstructPath(cameFrom, current.position);
            }

            for (int[] dir : DIRECTIONS) {
                Point neighbor = new Point(
                        current.position.x + dir[0],
                        current.position.y + dir[1]
                );

                if (isValid(neighbor) && !visited.contains(neighbor)) {
                    visited.add(neighbor);
                    cameFrom.put(neighbor, current.position);
                    queue.add(new Node(neighbor, current.cost + 1));
                }
            }
        }
        return Collections.emptyList(); // No path found
    }

    private List<Point> reconstructPath(Map<Point, Point> cameFrom, Point current) {
        List<Point> path = new LinkedList<>();
        while (cameFrom.containsKey(current)) {
            path.add(0, current);
            current = cameFrom.get(current);
        }
        return path;
    }

    private boolean isValid(Point p) {
        return CollisionManager.canMoveTo(p.x, p.y) &&
                p.x >= 0 && p.x < GameConstants.MAP_COLS &&
                p.y >= 0 && p.y < GameConstants.MAP_ROWS;
    }

    private static class Node {
        Point position;
        int cost;

        Node(Point position, int cost) {
            this.position = position;
            this.cost = cost;
        }
    }
}