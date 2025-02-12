package src.main;

import src.ui.GameFrame;

public class Laucher{
    public static void main(String[] args){
        javax.swing.SwingUtilities.invokeLater(() -> {
            GameFrame frame = new GameFrame();
            frame.setVisible(true);
        });;
    }
}