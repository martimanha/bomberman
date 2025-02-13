package src.main;

import src.ui.GameFrame;
import javax.swing.SwingUtilities;

public class Launcher{
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameFrame gameFrame = new GameFrame();
            gameFrame.setVisible(true);
            gameFrame.setLocationRelativeTo(null);
        });
    }
}