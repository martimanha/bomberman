package bomberman;

import javax.swing.JFrame;

public class Window extends JFrame {
    private final GamePanel gamePanel;

    public Window() {
        super("Bomberman");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        gamePanel = new GamePanel();
        add(gamePanel);
        pack();

        setLocationRelativeTo(null);
        setVisible(true);

        startGame();
    }

    private void startGame() {
        gamePanel.startGameThread();
    }

    public static void main(String[] args) {
        new Window();
    }
}