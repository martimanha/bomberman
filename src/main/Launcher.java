package src.main;

import src.ui.GameFrame;
import javax.swing.SwingUtilities;

public class Launcher{
<<<<<<< Updated upstream
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameFrame gameFrame = new GameFrame();
            gameFrame.setVisible(true);
            gameFrame.setLocationRelativeTo(null);
=======
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            GameFrame gameFrame = new GameFrame();
            gameFrame.setVisible(true);

            //CENTRALIZA A JANELA
            gameFrame.setLocationRelativeTo(null);
            gameFrame.showMenu();
>>>>>>> Stashed changes
        });
    }
}