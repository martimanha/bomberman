package src.ui;

import src.logic.MapLoader;
import javax.swing.*;
import java.awt.*;

public class GameModeSelectionPanel extends JPanel{
    public GameModeSelectionPanel(GameFrame frame){
        setLayout(new GridLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        //Titulo
        JLabel title = new JLabel(new ImageIcon(getClass().getResource(""))); //ALTERAR PELO RESPETIVO FICHEIRO
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(title, gbc);

        //Modos de jogo
        String[] modes = {"/src/resources/GameModeSelectionPanel/normalButton.png", "/src/resources/GameModeSelectionPanel/temporizadoButton.png", "/src/resources/GameModeSelectionPanel/labirintoButton.png", "/src/resources/GameModeSelectionPanel/dificilButton.png"};
        for(int i=0; i < modes.length; i++){
            JButton modeButton = createButton(modes[i], null);
            gbc.gridy++;
            add(modeButton, gbc);
        }

        //BOTOES INFERIORES
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        JButton backButton = createButton("/src/resources/GameModeSelectionPanel/voltarButton.png", e -> frame.showMenu()); //SUBSTITUIR PELO RESPETIVO FICHEIRO
        JButton playButton = createButton("/src/resources/GameModeSelectionPanel/playButton.png", e -> iniciarJogo(frame)); //SUBSTITUIR PELO RESPETIVO FICHEIRO

        bottomPanel.add(backButton);
        bottomPanel.add(playButton);

        gbc.gridy = modes.length + 1;
        add(bottomPanel, gbc);
    }

    private JButton createButton(String imagePath, java.awt.event.ActionListener action){
        JButton button = new JButton(new ImageIcon(getClass().getResource(imagePath)));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        if(action != null) button.addActionListener(action);
        return button;
    }

    public void iniciarJogo(GameFrame frame){
        frame.showMenu(); //TEMPORARIOOOOOOOOOOOOOOOOOOOO
    }
}