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
        
        
    }
}