package src.main;

import src.ui.GameFrame;
import src.ui.GamePanel;

import javax.swing.*;

public class Laucher{
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Bomberman");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.add(new GamePanel());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}