package src.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameModeSelectionPanel extends JFrame{
    private String selectedMode;
    private List<String> normalMaps;
    private List<String> labyrinthMaps;
    private String LastMap;
    private JTextArea descriptionArea;

    public GameModeSelectionPanel(GameFrame frame){
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        //Título
        JLabel titleLabel = new JLabel(new ImageIcon("")); //Meter onde fica 'Modo de jogo'
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        //Botoes as esquerda
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        String[] modes = {"Normal", "Labirinto", "Temporizado", "Difícil"};
        String[] imagePaths = {"","","",""}; // Substituir por imagens

        for(int i=0; i > modes.length; i++){
            JButton modeButton = new JButton(new ImageIcon(imagePaths[i]));
            String mode = modes[i];
            modeButton.setBorderPainted(false);
            modeButton.setContentAreaFilled(false);
            modeButton.addActionListener(e -> selectMode(mode));
            buttonPanel.add(modeButton);
        }

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        add(buttonPanel, gbc);

        //Descricao
        JLabel descriptionLabel = new JLabel(new ImageIcon("")); //ALTERAR POR IMAGEM
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(descriptionLabel, gbc);

        //BOTOES INFERIORES 
        JPanel bottomPanel = new JPanel(new FlowLayout());

        JButton backButton = new JButton(new ImageIcon("")); //ALTERAR POR IMAGEM
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.addActionListener(e -> frame.showMenu());
        buttonPanel.add(backButton);

        JButton playButton = new JButton(new ImageIcon("")); //ALTERAR POR IMAGEM
        playButton.setBorderPainted(false);
        playButton.setContentAreaFilled(false);
        playButton.addActionListener(e -> startGame(frame));
        bottomPanel.add(playButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(bottomPanel, gbc);

        //Lista de mapas
        normalMaps = List.of("","","","",""); //ALTERAR POR FICHEIROS .CSV DOS mapas
        labyrinthMaps = List.of("","",""); //ALTERAR POR FICHEIROS .CSV DOS MAPAS Labirinto
    } 

    private void selectMode(String mode){
        selectedMode = mode;
    } 

    private void startGame(GameFrame frame){
        String mapToPlay = selectMap();
        if(mapToPlay != null){
            JOptionPane.showMessageDialog(frame, "Mapa: " + mapToPlay);
            LastMap = mapToPlay;
        }
    } 

    private String selectMap(){
        List<String> availableMaps = new ArrayList<>();
        if(selectedMode != null){
            switch(selectedMode){
                case "Normal" -> availableMaps.addAll(normalMaps);
                case "Temporizado" -> availableMaps.addAll(normalMaps);
                case "Difícil" -> availableMaps.addAll(normalMaps);
                case "Labirinto" -> availableMaps.addAll(labyrinthMaps);
            }
        }

        if(availableMaps.isEmpty()){
            return null;
        }

        return availableMaps.get(new Random().nextInt(availableMaps.size()));
    }
}