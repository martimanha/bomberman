package src.ui;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame{
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private MenuPanel menuPanel;
    private GameModeSelectionPanel gameModeSelectionPanel;
    private RulesPanel rulesPanel;
    private SettingsPanel settingsPanel;

    public GameFrame(){
        setTitle("Bomberman");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,600);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        menuPanel = new MenuPanel(this);
        gameModeSelectionPanel = new GameModeSelectionPanel(this);
        rulesPanel = new RulesPanel(this);
        settingsPanel = new SettingsPanel(this);

        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(gameModeSelectionPanel, "GameModeSelection");
        mainPanel.add(rulesPanel, "Rules");
        mainPanel.add(SettingsPanel, "Settings");

        add(mainPanel);
        showMenu();
    }

    public void showMenu(){
        cardLayout.show(mainPanel, "Menu");
    }

    public void showGameModeSelection(){
        cardLayout.show(mainPanel, "GameModeSelection");
    }

    public void showRules(){
        cardLayout.show(mainPanel, "Rules");
    }

    public void showSettings(){
        cardLayout.show(mainPanel, "Settings");
    }
}