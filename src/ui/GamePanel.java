package src.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import src.logic.MapLoader;
import src.entities.Player;

public class GamePanel extends JPanel implements KeyListener, Runnable{
    private Thread gameThread;
    private Player player;
    private MapLoader maploader;

    public GamePanel() {
        this.setPreferredSize(new Dimension (800,600));
        this.setBackground(Color.LIGHT_GRAY);
        this.setFocusable(true);
        this.addKeyListener(this);

        mapLoader = new MapLoader(""); // ip do ficheiro necessário.
        player =new Player(50,50);

        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawMap(g);

        // Mostra o jogador
        g.setColor(Color.BLUE);
        g.fillRect(player.getX(), player,getY(), 40, 40);
    }

    private void drawMap(Graphics g) {
        int titleSize =40;
        char[][] mapGrid = maploader.getMap();

        for (int row = 0; row < mapGrid.length; row++){
            for (int col = 0; col < mapGrid[row].length; col++){
                char tile = mapGrid[row][col];
                if (tile == '#') {
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);
                  } else if (tile == 'B') {
                    g.setColor(Color.ORANGE);
                    g.fillRect(col* tileSize, row * tileSize, tileSize, tileSize);
                }   
            }
        }

        Override
        public void run() {
            while (true) {
                repaint();
                try { Thread.sleep(16);}
                catch (interruptedException e)
                { e.printStackTrace();}
            }
        }

        Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEnvent.VK_W) player.move(0, -1);
            if (key == KeyEvent.VK_S) player.move(0, 1);
            if (key == KeyEvent.VK_A) player.move(-1, 0);
            if (key == KeyEvent.VK_D) player.move(1, 0);
        }

        Override
        public void keyReleased(KeyEvent e) {}
        Override
        public void keyTyped(KeyEvent e) {}
    }
}