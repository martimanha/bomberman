package src.ui;

import src.entities.*;
import src.logic.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements KeyListener, ActionListener {
    private final int TILE_SIZE = 40;
    private final int FPS = 60;

    private GameFrame frame;
    private Player player;
    private Enemy[] enemy;
    private Bomb[] bomb;
    private Explosion[] explosion;
    private PowerUps[] powerUps;
    private Timer gameTimer;
    private MapLoader mapLoader;
    private CollisionManager collisionManager;
    private PowerUpsManager powerUpsManager;
    private AssertionLoader assertionLoader;
    
    public GamePanel(GameFrame frame) {
        this.frame = frame;
        setPreferredSize(new Dimension(800, 600));
        setFocusable(true);
        addKeyListener(this);

        initializeGameComponents();
        setupGameLoop(); 
    }

    private void initializeGameComponents() {
        //Carrega os recursos do jogo.
        assetsLoader = new AssetsLoader();

        //Inicializa o sistema.
        mapLoader = new MapLoader();
        collisionManager = new CollisionManager();
        powerUpsManager = new PowerUpsManager();

        //Carrega o mapa.
        mapLoader.loadMap("resources/maps/NormalMap1.csv");

        //inicializar o jogador.
        player= new Player(1 * TILE_SIZE, 1 * TILE_SIZE);

        //inicializar os inimigos.
        enemies = new Enemy[3];
        for(int i = 0; i < enemies.length; i++) {
            enemies[i] = new Enemy(1 * TILE_SIZE, (i+3) * TILE_SIZE);
        }
        
        bombs = new Bomb[10];
        explosions = new Explosion[10];
        powerUps = new PowerUps[5];
    }

    private void setupGameLoop() {
        gameTimer = new Timer(1000 / FPS, this);
        gameTimer.start();
    }

    Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            //Vai desenhar o mapa.
            drawMap(g2d);

            //Vai desenhar as entidades
            player.draw(g2d, assetsLoader.getPlayerImage());

            for(Enemy enemy : enemies) {
                if(enemy != null) enemy.draw(g2d, assetsLoader.getEnemyImage());
            }

            for(Bom bomb : bombs) {
                if(bomb != null) bomb.draw(g2d, assetsLoader.getBombImage());
            }

            for(Explosion explosion : explosions) {
                if(explosion != null) explosion.draw(g2d, assetsLoader.getExplosionImage());
            }

            for(PowerUps powerUp : powerUps) {
                if(powerUp != null) powerUp.draw(g2d, assetsLoader.getPowerUpImage());
            }
    }

    private void drawMap(Graphics2D g2d) {
        int[][] map = mapLoader.getMap();
        for(int y = 0; y < map.length; y++) {
            for(int x = 0; x < map.lenght; y++) {
                Image image = switch(map[y][x]) {
                    case 1 -> assetsLoader.getWallImage();
                    case 2 -> assetsLoader.getBreakableBlockImage();
                    default -> null;
                };

                if(image != null) {
                    g2d.drawImage(image, x *TILE_SIZE, y * TILE:SIZE, TILE_SIZE, TILE_SIZE, null);
                }
            }
        }
    }

    Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint();
    }

    private void updateGameState() {
        //Atualiza o jogador.
        player.update();

        //Atualiza os inimigos.
        for(Enemy enemy : enemies) {
            if (enemy != null) enemy.update();
        }
        
        //Verificar colisões
        collisionManager.checkPlayerCollisions(player, mapLoader.getMao());

        //Atualiza as bombas.
        for(int i = 0; i < bombs.length; i++) {
            if(bombs[i] != null && bombs[i].isExploded()) {
                createExplosion(bombs[i]);
                bombs[i] = null;
            }
        }
    }

    private void createExplosion(Bomb bomb) {
        // Lógica para criar a explosão
        for(int i = 0; i < explosions.length; i++) {
            if(explosions[i] == null) {
                explosions[i] = new Explosion(bomb.getX()), bomb.getY(), bomb.getBlastRadius());
                break;
            }
        }
    }

    Override
    public void keyPressed(KeyEvent e) {
        int ley = e.getKeyCode();

        //Movimento do jogador.
        if(key == KeyEvent.VK_W || key == KeyEvent.VK_UP) player.move(0, -TILE_SIZE);
        if(key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) player.move(0, TILE_SIZE);
        if(key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) player.move(-TILE_SIZE, 0);
        if(key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) player.move(TILE_SIZE, 0);

        // Colocar bomba.
        if(key == KeyEvent.VK_SPACE) {
            placeBomb(player.getX(), player.getY());
        }
    }

    private void placeBomb(int x, int y) {
        for (int i = 0; i < bombs.length; i++) {
            if(bombs[i] == null) {
                bombs[i] = new Bomb(x, y);
                break;
            }
        }
    }

    //Métodos não utilizados da interface KeyListener.
    Override public void keyTyped(KeyEvent e) {}
    Override public void keyReleased(KeyEvent e) {}
}