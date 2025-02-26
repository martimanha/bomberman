package bomberman;

import bomberman.entities.*;
import bomberman.managers.*;
import bomberman.powerups.PowerUp;
import bomberman.rendering.*;
import bomberman.ui.*;
import bomberman.utils.SpriteLoader;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import static bomberman.GameConstants.*;

public class GamePanel extends JPanel implements Runnable {
    private Thread gameThread;
    private boolean isRunning;
    private final InputKey inputKey;

    private final MainMenuState mainMenuState;
    private final RulesMenuState rulesMenuState;
    private final GameOverState gameOverState;
    private final LevelCompleteMenu levelCompleteMenu;
    private final GameCompletedState gameCompletedState;

    private Player player;
    private List<Enemy> enemies;
    private List<Bomb> bombs;
    private List<Explosion> explosions;
    private List<PowerUp> powerUps;

    private final MapManager mapManager;
    private final TimerManager timerManager;
    private char[][] gameMap;
    private final BufferedImage[] blockSprites = new BufferedImage[2];
    private BufferedImage floorSprite;
    private String currentMapFile;

    public GamePanel() {
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setDoubleBuffered(true);

        inputKey = new InputKey();
        mapManager = new MapManager();
        timerManager = new TimerManager(LEVEL_TIME);

        mainMenuState = new MainMenuState();
        rulesMenuState = new RulesMenuState();
        gameOverState = new GameOverState();
        levelCompleteMenu = new LevelCompleteMenu();
        gameCompletedState = new GameCompletedState();

        startNewGame();
        setupInputHandling();
        loadSprites();
    }

    private void setupInputHandling() {
        addKeyListener(inputKey);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleUIInteraction(e);
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                updateUIHoverStates(e.getPoint());
            }
        });
        setFocusable(true);
    }

    private void startNewGame() {
        currentMapFile = mapManager.getRandomMap();
        initializeGame(currentMapFile);
    }

    private void initializeGame(String mapFile) {
        MapLoader.LoadResult result = MapLoader.loadMap(mapFile);
        gameMap = result.map;
        enemies = result.enemies;
        player = new Player(result.playerX, result.playerY, new StatusManager());
        bombs = new ArrayList<>();
        explosions = new ArrayList<>();
        powerUps = new ArrayList<>();

        CollisionManager.initialize(gameMap, bombs, powerUps, explosions);
        timerManager.reset();
    }

    private void loadSprites() {
        floorSprite = SpriteLoader.loadSprite("/sprites/blocks/floor.png");
        blockSprites[0] = SpriteLoader.loadSprite("/sprites/blocks/indestructible.png");
        blockSprites[1] = SpriteLoader.loadSprite("/sprites/blocks/destructible.png");
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        isRunning = true;
        gameThread.start();
    }

    @Override
    public void run() {
        double interval = 1000000000.0 / 60;
        double delta = 0;
        long lastTime = System.nanoTime();

        while (isRunning) {
            long currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / interval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    private void update() {
        if (shouldUpdateGame()) {
            handleInput();
            checkLevelTransition();
            updatePlayer();
            updateEnemies();
            updateBombs();
            updateExplosions();
            updatePowerUps();
            checkCollisions();
            checkGameOver();
        }
    }

    private boolean shouldUpdateGame() {
        return !mainMenuState.isActive()
                && !rulesMenuState.isActive()
                && !gameOverState.isActive()
                && !levelCompleteMenu.isActive()
                && !gameCompletedState.isActive();
    }

    private void handleInput() {
        if (!player.isAlive() || levelCompleteMenu.isActive() || gameCompletedState.isActive()) return;

        if (inputKey.isKeyPressed(KeyEvent.VK_W)) tryMove(Player.Direction.UP);
        if (inputKey.isKeyPressed(KeyEvent.VK_S)) tryMove(Player.Direction.DOWN);
        if (inputKey.isKeyPressed(KeyEvent.VK_A)) tryMove(Player.Direction.LEFT);
        if (inputKey.isKeyPressed(KeyEvent.VK_D)) tryMove(Player.Direction.RIGHT);

        if (inputKey.getLastKeyPressed() == KeyEvent.VK_SPACE) {
            placeBomb();
            inputKey.clearLastKeyPressed();
        }
    }

    private void tryMove(Player.Direction dir) {
        int newX = player.getXTile() + dir.xOffset;
        int newY = player.getYTile() + dir.yOffset;

        if (CollisionManager.canMoveTo(newX, newY, player.isInvulnerable())) {
            player.move(dir);
        }
    }

    private void updatePlayer() {
        player.update();
    }

    private void updateEnemies() {
        enemies.removeIf(enemy -> !enemy.isAlive());
        enemies.forEach(enemy -> enemy.update(player));
    }

    private void updateBombs() {
        bombs.forEach(bomb -> {
            bomb.setExplosionsList(explosions);
            bomb.update();
        });
        bombs.removeIf(Bomb::hasExploded);
    }

    private void updateExplosions() {
        explosions.removeIf(Explosion::isFinished);
    }

    private void updatePowerUps() {
        new PowerUpManager(powerUps, player).update();
    }

    private void checkCollisions() {
        checkPlayerExplosionCollision();
        checkEnemyCollision();
    }

    private void checkPlayerExplosionCollision() {
        if (!player.isInvulnerable() &&
                CollisionManager.checkExplosionCollision(player.getXTile(), player.getYTile())) {
            player.takeDamage();
        }
    }

    private void checkEnemyCollision() {
        if (player.isInvulnerable()) return;

        enemies.stream()
                .filter(Enemy::isAlive)
                .filter(e -> e.getXTile() == player.getXTile() && e.getYTile() == player.getYTile())
                .findFirst()
                .ifPresent(e -> player.takeDamage());
    }

    private void placeBomb() {
        if (System.currentTimeMillis() - player.getLastBombTime() < 3000) return;

        int x = player.getXTile();
        int y = player.getYTile();

        boolean canPlace = bombs.stream()
                .noneMatch(b -> b.getXTile() == x && b.getYTile() == y);

        if (canPlace) {
            Bomb newBomb = new Bomb(x, y, player.getStatusManager().getBombPower());
            newBomb.setExplosionsList(explosions);
            bombs.add(newBomb);
            player.setLastBombTime(System.currentTimeMillis());
        }
    }

    private void checkLevelTransition() {
        int playerX = player.getXTile();
        int playerY = player.getYTile();

        if (gameMap[playerY][playerX] == 'S' && !levelCompleteMenu.isActive()) {
            if (mapManager.getRemainingMaps() == 0) {
                gameCompletedState.activate();
            } else {
                levelCompleteMenu.activate();
            }
        }
    }

    private void loadNextLevel() {
        currentMapFile = mapManager.getRandomMap();
        initializeGame(currentMapFile);
        levelCompleteMenu.deactivate();
        timerManager.reset();
    }

    private void checkGameOver() {
        if ((!player.isAlive() || timerManager.isTimeUp()) && !gameOverState.isActive()) {
            triggerGameOver();
        }
    }

    private void triggerGameOver() {
        gameOverState.activate();
        requestFocus();
    }

    private void resetGame() {
        mapManager.resetMaps();
        startNewGame();
        gameOverState.deactivate();
        isRunning = true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        renderGameWorld(g2);
        renderUIElements(g2);

        g2.dispose();
    }

    private void renderGameWorld(Graphics2D g2) {
        MapRenderer.render(g2, gameMap, floorSprite, blockSprites);
        EntityRenderer.drawBombs(g2, bombs);
        EntityRenderer.drawPowerUps(g2, powerUps);
        EntityRenderer.drawPlayer(g2, player);
        EntityRenderer.drawEnemies(g2, enemies);
        EntityRenderer.drawExplosions(g2, explosions);
        HUDRenderer.render(g2, player.getStatusManager(), player, timerManager);
    }

    private void renderUIElements(Graphics2D g2) {
        if (mainMenuState.isActive()) {
            mainMenuState.render(g2);
        } else if (rulesMenuState.isActive()) {
            rulesMenuState.render(g2);
        } else if (gameOverState.isActive()) {
            gameOverState.render(g2);
        } else if (levelCompleteMenu.isActive()) {
            levelCompleteMenu.render(g2);
        } else if (gameCompletedState.isActive()) {
            gameCompletedState.render(g2);
        }
    }

    private void handleUIInteraction(MouseEvent e) {
        if (mainMenuState.isActive()) {
            mainMenuState.handleClick(e, rulesMenuState);
        } else if (rulesMenuState.isActive()) {
            rulesMenuState.handleClick(e);
        } else if (gameOverState.isActive()) {
            handleGameOverClick(e);
        } else if (levelCompleteMenu.isActive()) {
            handleLevelCompleteClick(e);
        } else if (gameCompletedState.isActive()) {
            handleGameCompletedClick(e);
        }
    }

    private void handleGameOverClick(MouseEvent e) {
        if (gameOverState.getRetryButton().isClicked(e)) {
            resetGame();
        } else if (gameOverState.getExitButton().isClicked(e)) {
            mainMenuState.setActive(true);
            gameOverState.deactivate();
        }
    }

    private void handleLevelCompleteClick(MouseEvent e) {
        if (levelCompleteMenu.getNextLevelButton().isClicked(e)) {
            loadNextLevel();
        } else if (levelCompleteMenu.getExitButton().isClicked(e)) {
            mainMenuState.setActive(true);
            levelCompleteMenu.deactivate();
        }
    }

    private void handleGameCompletedClick(MouseEvent e) {
        if (gameCompletedState.getMenuButton().isClicked(e)) {
            mainMenuState.setActive(true);
            gameCompletedState.deactivate();
            mapManager.resetMaps();
        }
    }

    private void updateUIHoverStates(Point mousePos) {
        if (mainMenuState.isActive()) {
            mainMenuState.handleMouseMove(mousePos);
        } else if (rulesMenuState.isActive()) {
            rulesMenuState.handleMouseMove(mousePos);
        } else if (gameOverState.isActive()) {
            gameOverState.handleMouseMove(mousePos);
        } else if (levelCompleteMenu.isActive()) {
            levelCompleteMenu.handleMouseMove(mousePos);
        } else if (gameCompletedState.isActive()) {
            gameCompletedState.handleMouseMove(mousePos);
        }
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void fullReset() {
        mapManager.resetMaps();
        startNewGame();
        gameOverState.deactivate();
        levelCompleteMenu.deactivate();
        gameCompletedState.deactivate();
        mainMenuState.setActive(false);
        isRunning = true;
    }

}