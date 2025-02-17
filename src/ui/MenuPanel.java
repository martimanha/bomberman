package src.ui;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel{
    public MenuPanel(GameFrame frame){
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        //Background
        JLabel background = new JLabel(new ImageIcon(getClass().getResource("/MenuPanel/background.jpg")));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(background, gbc);

        //Botoes
        JButton playButton = createButton("/src/resources/MenuPanel/playButton.png", e -> frame.showGameModeSelection());
        JButton rulesButton = createButton("/src/resources/MenuPanel/rulesButton.png", e -> frame.showRules());
        JButton settingsButton = createButton("/src/resources/MenuPanel/settingsButton.png", e -> frame.showSettings());

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