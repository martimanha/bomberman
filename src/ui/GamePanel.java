package src.ui;

import src.entities.Player;
import src.logic.MapLoader;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements KeyListener, ActionListener {
    private final int TILE_SIZE = 40;
    private Timer gameTimer;
    private Player player;
    private MapLoader mapLoader;
    private AssetsLoader assetsLoader;

    public GamePanel(GameFrame frame) {
        setPreferredSize(new Dimension(800, 600));
        setFocusable(true);
        addKeyListener(this);
        initializeGame();
    }

    private void initializeGame() {
        assetsLoader = new AssetsLoader();
        mapLoader = new MapLoader();
        mapLoader.loadMap("resources/maps/NormalMap1.csv");
        player = new Player(1 * TILE_SIZE, 1 * TILE_SIZE);
        gameTimer = new Timer(1000/60, this);
        gameTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Desenhar mapa
        drawMap(g2d);
        
        // Desenhar jogador
        player.draw(g2d, assetsLoader.getPlayerImage());
    }

    private void drawMap(Graphics2D g2d) {
        int[][] map = mapLoader.getMap();
        for(int y = 0; y < map.length; y++) {
            for(int x = 0; x < map[y].length; x++) {
                Image image = switch(map[y][x]) {
                    case 1 -> assetsLoader.getWallImage();
                    default -> null;
                };
                
                if(image != null) {
                    g2d.drawImage(image, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        int speed = TILE_SIZE;
        
        if(key == KeyEvent.VK_W) player.move(0, -speed);
        if(key == KeyEvent.VK_S) player.move(0, speed);
        if(key == KeyEvent.VK_A) player.move(-speed, 0);
        if(key == KeyEvent.VK_D) player.move(speed, 0);
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}