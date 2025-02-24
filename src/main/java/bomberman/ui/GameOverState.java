package bomberman.ui;

import static bomberman.GameConstants.SCREEN_HEIGHT;
import static bomberman.GameConstants.SCREEN_WIDTH;

import bomberman.utils.UILoader;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class GameOverState {
    private BufferedImage background;
    private Button retryButton;
    private Button exitButton;
    private boolean isActive;

    public GameOverState() {
        isActive = false;
        loadSprites();
        createButtons();
    }

    private void loadSprites() {
        background = UILoader.loadUISprite("gameOver_menu/gameOver.png");
    }

    private void createButtons() {
        int buttonWidth = 200;
        int buttonSpacing = 30;
        int totalWidth = buttonWidth * 2 + buttonSpacing;

        int startX = (SCREEN_WIDTH - totalWidth) / 2;
        int buttonY = (int) (SCREEN_HEIGHT * 0.7);

        retryButton = new Button(
                "gameOver_menu/retry.png",
                "gameOver_menu/retry_hover.png",
                startX, buttonY
        );

        exitButton = new Button(
                "gameOver_menu/exit.png",
                "gameOver_menu/exit_hover.png",
                startX + buttonWidth + buttonSpacing, buttonY
        );
    }

    public void handleMouseMove(Point mousePosition) {
        retryButton.update(mousePosition);
        exitButton.update(mousePosition);
    }

    public void handleClick(MouseEvent e) {
        if (retryButton.isClicked(e)) {
            isActive = false;
        } else if (exitButton.isClicked(e)) {
            System.exit(0);
        }
    }

    public void render(Graphics2D g2) {
        if (background != null) {
            // Centralizar a imagem de fundo
            int bgX = (SCREEN_WIDTH - background.getWidth()) / 2;
            int bgY = (SCREEN_HEIGHT - background.getHeight()) / 3;
            g2.drawImage(background, bgX, bgY, null);
        }

        // Renderizar botões
        retryButton.render(g2);
        exitButton.render(g2);
    }

    public boolean isActive() {
        return isActive;
    }

    public void activate() {
        isActive = true;
    }

    public void deactivate() {
        isActive = false;
    }

    // Novos getters para os botões
    public Button getRetryButton() {
        return retryButton;
    }

    public Button getExitButton() {
        return exitButton;
    }
}