package bomberman.ui;

import bomberman.utils.UILoader;

import java.awt.Point;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import static bomberman.GameConstants.*;

public class GameCompletedState {
    private BufferedImage title;
    private Button menuButton;
    private boolean isActive;

    // Layout consistente com outros menus
    private static final int BUTTON_WIDTH = 200;
    private static final int VERTICAL_OFFSET = 270;
    private static final int TITLE_Y = 100;

    public GameCompletedState() {
        isActive = false;
        loadSprites();
        createButton();
    }

    private void loadSprites() {
        title = UILoader.loadUISprite("gameCompleted_menu/gameCompleted.png");
    }

    private void createButton() {
        int buttonX = (SCREEN_WIDTH - BUTTON_WIDTH) / 2;
        int buttonY = SCREEN_HEIGHT - VERTICAL_OFFSET;

        menuButton = new Button(
                "gameCompleted_menu/menu.png",
                "gameCompleted_menu/menu_hover.png",
                buttonX,
                buttonY
        );
    }

    public void handleMouseMove(Point mousePosition) {
        menuButton.update(mousePosition);
    }

    public void handleClick(MouseEvent e) {
        if (menuButton.isClicked(e)) {
            isActive = false;
        }
    }

    public void render(Graphics2D g2) {
        if (title != null) {
            int titleX = (SCREEN_WIDTH - title.getWidth()) / 2;
            g2.drawImage(title, titleX, TITLE_Y, null);
        }
        menuButton.render(g2);
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

    public Button getMenuButton() {
        return menuButton;
    }
}