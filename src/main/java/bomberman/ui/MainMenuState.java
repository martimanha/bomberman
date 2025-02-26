package bomberman.ui;

import static bomberman.GameConstants.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import bomberman.utils.UILoader;

public class MainMenuState {
    private BufferedImage background;
    private BufferedImage title;
    private BufferedImage subtitle;
    private Button playButton;
    private Button rulesButton;
    private Button exitButton;
    private boolean isActive;

    public MainMenuState() {
        loadResources();
        initializeButtons();
        isActive = true;
    }

    private void loadResources() {
        background = UILoader.loadUISprite("main_menu/background.png");
        title = UILoader.loadUISprite("main_menu/title.png");
        subtitle = UILoader.loadUISprite("main_menu/subtitle.png");
    }

    private void initializeButtons() {
        int buttonWidth = 200;
        int startX = (SCREEN_WIDTH - buttonWidth) / 2 - 20;
        int playY = 300;
        int rulesY = 385;
        int exitX = startX + 125;

        playButton = new Button(
                "main_menu/buttons/play.png",
                "main_menu/buttons/play_hover.png",
                startX, playY
        );

        rulesButton = new Button(
                "main_menu/buttons/rules.png",
                "main_menu/buttons/rules_hover.png",
                startX, rulesY
        );

        exitButton = new Button(
                "main_menu/buttons/exit.png",
                "main_menu/buttons/exit_hover.png",
                exitX, rulesY
        );
    }

    public void handleClick(MouseEvent e, RulesMenuState rulesMenu) {
        if (playButton.isClicked(e)) {
            isActive = false;
        } else if (rulesButton.isClicked(e)) {
            isActive = false;
            rulesMenu.setActive(true);
        } else if (exitButton.isClicked(e)) {
            System.exit(0);
        }
    }

    public void handleMouseMove(Point mousePosition) {
        playButton.update(mousePosition);
        rulesButton.update(mousePosition);
        exitButton.update(mousePosition);
    }

    public void render(Graphics2D g2) {
        g2.drawImage(background, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);

        int titleX = (SCREEN_WIDTH - title.getWidth()) / 2;
        g2.drawImage(title, titleX, 100, null);

        int subtitleX = (SCREEN_WIDTH - subtitle.getWidth()) / 2;
        g2.drawImage(subtitle, subtitleX, 180, null);

        playButton.render(g2);
        rulesButton.render(g2);
        exitButton.render(g2);
    }

    public void reset() {
        playButton.reset();
        rulesButton.reset();
        exitButton.reset();
        isActive = true;
    }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}