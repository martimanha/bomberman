package src.ui;

import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel{
    public SettingsPanel(GameFrame frame){
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        //Titulo
        JLabel title = new JLabel(new ImageIcon(getClass().getResource(""))); //SUBSTITUIR PELO RESPETIVO FICHEIRO
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(title, gbc);

        JLabel volumeLabel = new JLabel(new ImageIcon(getClass().getResource(""))); //SUBSTITUIR PELO RESPETIVO FICHEIRO
        gbc.gridy = 1;
        add(volumeLabel, gbc);

        JSlider volumeSlider = new JSlider(0, 100, 50); //SUBSTITUIR PELO RESPETIVO FICHEIRO
        gbc.gridy = 2;
        add(volumeSlider, gbc);

        //IDIOMA

        JLabel idiomaLabel = new JLabel(new ImageIcon(getClass().getResource(""))); //SUBSTITUIR PELO RESPETIVO FICHEIRO
        gbc.gridy = 3;
        add(idiomaLabel, gbc);

        JButton portuguesButton = new JButton(new ImageIcon(getClass().getResource(""))); // SUBSTITUIR PELO RESPETIVO FICHEIRO
        gbc.gridy = 4;
        add(portuguesButton, gbc);

        JButton inglesButton = new JButton(new ImageIcon(getClass().getResource(""))); //SUBSTITUIR PELO RESPETIVO FICHEIRO\
        gbc.gridy = 5;
        add(inglesButton, gbc);

        // BOTAO VOLTAR (TEMPORARIOOOOOOOOOOOOOOOOOOOO)
        JButton backButton = new JButton(new ImageIcon(getClass().getResource(""))); //SUBSTITUIR PELO RESPETIVO FICHEIRO
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.addActionListener(e -> frame.showMenu());
        gbc.gridy = 6;
        add(backButton, gbc);
    }
}