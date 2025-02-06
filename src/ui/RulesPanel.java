package src.ui;

import javax.swing.*;
import java.awt.*;

public class RulesPanel extends JPanel{
    public RulesPanel(GameFrame frame){
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        //Titulo
        JLabel title = new JLabel(new ImageIcon(getClass().getResource(""))); //SUBSTITUIR PELO RESPETIVO FICHEIRO
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(title, gbc);

        //Botao voltar
        JButton backButton = new JButton(new ImageIcon(getClass().getResource(""))); //SUBSTITUIR PELO RESPETIVO NOME
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.addActionListener(e -> frame.showMenu());
        gbc.gridy = 1;
        add(backButton, gbc);
    }
}