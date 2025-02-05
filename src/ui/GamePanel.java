package src.ui;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;

public class GamePanel extends JPanel{
    public GamePanel(){
        this.setPreferredSize(new java.awt.Dimension(600,800));
        this.setBackground(Color.BLACK);
    }

    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.drawString("Bomberman Game", 350, 300);
    }
}