package bomberman.ui;

import static bomberman.GameConstants.SCREEN_HEIGHT;
import static bomberman.GameConstants.SCREEN_WIDTH;

import bomberman.utils.UILoader;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class RulesMenuState {
    private BufferedImage background;
    private BufferedImage description;
    private Button backButton;
    private boolean isActive;

    public RulesMenuState() {
        isActive = false;
        loadSprites();
        createButtons();
    }

    private void loadSprites() {
        background = UILoader.loadUISprite("rules_menu/background.png");
        description = UILoader.loadUISprite("rules_menu/description.png");
    }

    private void createButtons() {
        int backButtonWidth = 200;
        int backButtonX = (SCREEN_WIDTH - backButtonWidth)-650;
        int backButtonY = SCREEN_HEIGHT - 550;
        backButton = new Button(
                "rules_menu/buttons/back.png",
                "rules_menu/buttons/back_hover.png",
                backButtonX, backButtonY
        );
    }

    public void handleMouseMove(Point mousePosition) {
        backButton.update(mousePosition);
    }

    public void handleClick(MouseEvent e) {
        if (backButton.isClicked(e)) {
            isActive = false; // Volta ao menu principal
        }
    }

    public void render(Graphics2D g2) {
        // Renderiza o fundo
        g2.drawImage(background, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);

        // Renderiza a descrição centralizada
        if (description != null) {
            int descX = (SCREEN_WIDTH - description.getWidth()) / 2;
            int descY = (SCREEN_HEIGHT - description.getHeight()) / 2;
            g2.drawImage(description, descX, descY, null);
        }

        // Renderiza o botão "Voltar"
        backButton.render(g2);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}