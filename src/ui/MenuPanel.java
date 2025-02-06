package src.ui;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel{
    public MenuPanel(GameFrame frame){
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        //Background
        JLabel background = new JLabel(new ImageIcon(getClass().getResource(""))); //MUDAR PARA O FICHEIRO RESPETIVO
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(background, gbc);

        //Botoes, mudar todos os "" pelos arquivos respetivos
        JButton playButton = createButton("", e -> frame.showGameModeSelection());
        JButton rulesButton = createButton("", e -> frame.showRules());
        JButton settingsButton = createButton("", e -> frame.showSettings());

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        add(playButton, gbc);

        gbc.gridy = 2;
        add(rulesButton, gbc);

        gbc.gridy = 3;
        add(settingsButton, gbc);
    }

    private JButton createButton(String imagePath, java.awt.event.ActionListener action){
        JButton button = new JButton(new ImageIcon(getClass().getResource(imagePath)));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.addActionListener(action);
        return button;
    }
}