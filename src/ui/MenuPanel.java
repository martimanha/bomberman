package src.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel{
    public MenuPanel(GameFrame frame){
        this.setLayout(new GridLayout(3,1));

        JButton playButton = new JButton("Jogar");
        JButton rulesButton = new JButton("Regras");
        JButton settingsButton = new JButton("Definições");

        playButton.addActionListener(e -> frame.showGameModeSelection());
        rulesButton.addActionListener(e -> frame.showRules());
        settingsButton.addActionListener(e -> frame.showSettings());

        this.add(playButton);
        this.add(rulesButton);
        this.add(settingsButton);
    }
}