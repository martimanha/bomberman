package bomberman.ui;

import java.awt.Point;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import static bomberman.GameConstants.SCREEN_HEIGHT;
import static bomberman.GameConstants.SCREEN_WIDTH;
import bomberman.utils.UILoader;

public class LevelCompleteMenu {
    private BufferedImage title;
    private Button nextLevelButton;
    private Button exitButton;
    private boolean isActive;

    // Configurações idênticas ao GameOverState
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_SPACING = -40;
    private static final int VERTICAL_OFFSET = 270;
    private static final int TITLE_Y = 100;

    public LevelCompleteMenu() {
        isActive = false;
        loadSprites();
        createButtons();
    }

    private void loadSprites() {
        title = UILoader.loadUISprite("levelPassed_menu/LevelPassed.png");
    }

    private void createButtons() {
        int totalWidth = BUTTON_WIDTH * 2 + BUTTON_SPACING;
        int startX = ((SCREEN_WIDTH - totalWidth) / 2) + 45;
        int buttonY = SCREEN_HEIGHT - VERTICAL_OFFSET;

        nextLevelButton = new Button(
                "levelPassed_menu/nextLevel.png",
                "levelPassed_menu/nextLevel_hover.png",
                startX,
                buttonY
        );

        exitButton = new Button(
                "levelPassed_menu/exit.png",
                "levelPassed_menu/exit_hover.png",
                startX + BUTTON_WIDTH + BUTTON_SPACING,
                buttonY
        );
    }

    public void handleMouseMove(Point mousePosition) {
        nextLevelButton.update(mousePosition);
        exitButton.update(mousePosition);
    }

    public void handleClick(MouseEvent e) {
        if (nextLevelButton.isClicked(e)) {
            isActive = false;
        } else if (exitButton.isClicked(e)) {
            System.exit(0);
        }
    }

    public void render(Graphics2D g2) {
        if (title != null) {
            int titleX = (SCREEN_WIDTH - title.getWidth()) / 2;
            g2.drawImage(title, titleX, TITLE_Y, null);
        }

        nextLevelButton.render(g2);
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

    public Button getNextLevelButton() {
        return nextLevelButton;
    }

    public Button getExitButton() {
        return exitButton;
    }
}