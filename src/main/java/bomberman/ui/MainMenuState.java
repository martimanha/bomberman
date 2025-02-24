package bomberman.ui;

import static bomberman.GameConstants.SCREEN_HEIGHT;
import static bomberman.GameConstants.SCREEN_WIDTH;

import bomberman.utils.UILoader;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

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

        // Posicionamento original específico
        int playButtonX = (SCREEN_WIDTH - buttonWidth)/2 - 20;
        int playButtonY = 300;

        int rulesButtonX = (SCREEN_WIDTH - buttonWidth)/2 - 15;
        int rulesButtonY = 385;

        int exitButtonX = rulesButtonX + 125;

        playButton = new Button(
                "main_menu/buttons/play.png",
                "main_menu/buttons/play_hover.png",
                playButtonX,
                playButtonY
        );

        rulesButton = new Button(
                "main_menu/buttons/rules.png",
                "main_menu/buttons/rules_hover.png",
                rulesButtonX,
                rulesButtonY
        );

        exitButton = new Button(
                "main_menu/buttons/exit.png",
                "main_menu/buttons/exit_hover.png",
                exitButtonX,
                rulesButtonY
        );
    }

    public void handleMouseMove(Point mousePosition) {
        playButton.update(mousePosition);
        rulesButton.update(mousePosition);
        exitButton.update(mousePosition);
    }

    public void handleClick(MouseEvent e, RulesMenuState rulesMenu, GameSelectionMenuState gameSelection) {
        if (playButton.isClicked(e)) {
            isActive = false;
            gameSelection.setActive(true);
        } else if (rulesButton.isClicked(e)) {
            isActive = false;
            rulesMenu.setActive(true);
        } else if (exitButton.isClicked(e)) {
            System.exit(0);
        }
    }

    public void render(Graphics2D g2) {
        // Fundo
        g2.drawImage(background, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);

        // Título e subtítulo
        renderTitle(g2);

        // Botões nas posições originais
        playButton.render(g2);
        rulesButton.render(g2);
        exitButton.render(g2);
    }

    private void renderTitle(Graphics2D g2) {
        if (title != null && subtitle != null) {
            int titleX = (SCREEN_WIDTH - title.getWidth()) / 2;
            int subtitleX = (SCREEN_WIDTH - subtitle.getWidth()) / 2;

            g2.drawImage(title, titleX, 100, null);
            g2.drawImage(subtitle, subtitleX, 180, null);
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
        // Resetar estados ao reativar
        playButton.reset();
        rulesButton.reset();
        exitButton.reset();
    }
}