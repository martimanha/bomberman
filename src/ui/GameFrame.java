package src.ui;

import javax.swing.JFrame;

public class GameFrame extends JFrame {
    public GameFrame(){
        this.setTitle("Bomberman Game");

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            this.setResizable(false);
            this.setContentPane(new GamePanel());
            this.pack();
            this.setLocationRelativeTo(null); // Centraliza a janela.
    }
}
