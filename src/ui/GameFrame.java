package src.ui;

public class GameFrame {
    public GameFrame(){
        this.setTitle("Bomberman Game");

        this.setdefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setResizable(false);
            this.setContentPane(new GamePanel());
            this.pack();
            this.setLocationRelativeTo(null); // Centraliza a janela.
    }
}
