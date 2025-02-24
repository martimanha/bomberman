package bomberman;

import bomberman.entities.*;
import bomberman.managers.*;
import bomberman.powerups.PowerUp;
import bomberman.rendering.*;
import bomberman.ui.GameOverState;
import bomberman.ui.MainMenuState;
import bomberman.ui.RulesMenuState;
import bomberman.ui.GameSelectionMenuState;
import bomberman.utils.SpriteLoader;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static bomberman.GameConstants.*;

public class GamePanel extends JPanel implements Runnable {
    private Thread gameThread;
    private boolean isRunning;
    private final InputKey inputKey;

    // Estados da UI
    private final MainMenuState mainMenuState;
    private final GameSelectionMenuState gameSelectionState;
    private final RulesMenuState rulesMenuState;
    private final bomberman.ui.GameOverState gameOverState;

    // Entidades do jogo
    private Player player;
    private List<Enemy> enemies;
    private List<Bomb> bombs;
    private List<Explosion> explosions;
    private List<PowerUp> powerUps;

    // Gerenciadores
    private final MapManager mapManager;
    private char[][] gameMap;
    private final BufferedImage[] blockSprites = new BufferedImage[2];
    private BufferedImage floorSprite;
    private String currentMapFile;

    public GamePanel() {
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setDoubleBuffered(true);

        inputKey = new InputKey();
        mapManager = new MapManager();
        currentMapFile = mapManager.getRandomMap();

        // Inicializar estados UI
        mainMenuState = new MainMenuState();
        gameSelectionState = new GameSelectionMenuState(mainMenuState);
        rulesMenuState = new RulesMenuState();
        gameOverState = new bomberman.ui.GameOverState();

        initializeGame(currentMapFile);
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

    private void initializeGame(String mapFile) {
        MapLoader.LoadResult result = MapLoader.loadMap(mapFile);
        gameMap = result.map;
        enemies = result.enemies;
        player = new Player(result.playerX, result.playerY, new StatusManager());
        bombs = new ArrayList<>();
        explosions = new ArrayList<>();
        powerUps = new ArrayList<>();

        CollisionManager.initialize(gameMap, bombs, powerUps);
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
        }

        if (!player.isAlive() && !gameOverState.isActive()) {
            triggerGameOver();
        }
    }

    private boolean shouldUpdateGame() {
        return !mainMenuState.isActive()
                && !gameSelectionState.isActive()
                && !rulesMenuState.isActive()
                && !gameOverState.isActive();
    }

    private void handleInput() {
        if (inputKey.isKeyPressed(KeyEvent.VK_W)) player.move(Player.Direction.UP);
        if (inputKey.isKeyPressed(KeyEvent.VK_S)) player.move(Player.Direction.DOWN);
        if (inputKey.isKeyPressed(KeyEvent.VK_A)) player.move(Player.Direction.LEFT);
        if (inputKey.isKeyPressed(KeyEvent.VK_D)) player.move(Player.Direction.RIGHT);

        if (inputKey.getLastKeyPressed() == KeyEvent.VK_SPACE) {
            placeBomb();
            inputKey.clearLastKeyPressed();
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
        powerUps.removeIf(powerUp -> !powerUp.isActive());
    }

    private void checkCollisions() {
        checkPlayerExplosionCollision();
        checkEnemyCollision();
    }

    private void checkPlayerExplosionCollision() {
        if (CollisionManager.checkExplosionCollision(player.getXTile(), player.getYTile())) {
            player.takeDamage();
        }
    }

    private void checkEnemyCollision() {
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
        if (gameMap[player.getYTile()][player.getXTile()] == 'S') {
            resetGame();
        }
    }

    private void triggerGameOver() {
        gameOverState.activate();
        requestFocus();
    }

    private void resetGame() {
        currentMapFile = mapManager.getRandomMap();
        initializeGame(currentMapFile);
        loadSprites();
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
        if (shouldUpdateGame()) {
            MapRenderer.render(g2, gameMap, floorSprite, blockSprites);
            EntityRenderer.drawBombs(g2, bombs);
            EntityRenderer.drawPowerUps(g2, powerUps);
            EntityRenderer.drawPlayer(g2, player);
            EntityRenderer.drawEnemies(g2, enemies);
            EntityRenderer.drawExplosions(g2, explosions);
            HUDRenderer.render(g2, player.getStatusManager(), player);
        }
    }

    private void renderUIElements(Graphics2D g2) {
        if (mainMenuState.isActive()) {
            mainMenuState.render(g2);
        } else if (gameSelectionState.isActive()) {
            gameSelectionState.render(g2);
        } else if (rulesMenuState.isActive()) {
            rulesMenuState.render(g2);
        } else if (gameOverState.isActive()) {
            renderGameOverOverlay(g2);
        }
    }

    private void renderGameOverOverlay(Graphics2D g2) {
        // Fundo semi-transparente
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        // Elementos do Game Over
        gameOverState.render(g2);
    }

    private void handleUIInteraction(MouseEvent e) {
        if (mainMenuState.isActive()) {
            mainMenuState.handleClick(e, rulesMenuState, gameSelectionState);
        } else if (gameSelectionState.isActive()) {
            gameSelectionState.handleClick(e);
        } else if (rulesMenuState.isActive()) {
            rulesMenuState.handleClick(e);
        } else if (gameOverState.isActive()) {
            handleGameOverClick(e);
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

    private void updateUIHoverStates(Point mousePos) {
        if (mainMenuState.isActive()) {
            mainMenuState.handleMouseMove(mousePos);
        } else if (gameSelectionState.isActive()) {
            gameSelectionState.handleMouseMove(mousePos);
        } else if (rulesMenuState.isActive()) {
            rulesMenuState.handleMouseMove(mousePos);
        } else if (gameOverState.isActive()) {
            gameOverState.handleMouseMove(mousePos);
        }
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }
}