package bomberman.rendering;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class GameOverMenu {
    private static final int OPTION_HEIGHT = 40;
    private static final int MENU_WIDTH = 200;
    private int selectedOption = 0;
    private Font menuFont;

    public GameOverMenu() {
        try {
            menuFont = Font.createFont(Font.TRUETYPE_FONT,
                            getClass().getResourceAsStream("/fonts/FredokaOne-Regular.ttf"))
                    .deriveFont(Font.BOLD, 24f);
        } catch (Exception e) {
            menuFont = new Font("Arial", Font.BOLD, 24);
        }
    }

    public void handleMouseClick(MouseEvent e) {
        int mouseY = e.getY();
        int menuY = e.getComponent().getHeight() / 2;

        if (mouseY >= menuY && mouseY <= menuY + OPTION_HEIGHT) {
            selectedOption = 0;
        } else if (mouseY >= menuY + OPTION_HEIGHT && mouseY <= menuY + 2 * OPTION_HEIGHT) {
            selectedOption = 1;
        }
    }

    public void handleKeyInput(int keyCode) {
        if (keyCode == KeyEvent.VK_UP) {
            selectedOption = Math.max(0, selectedOption - 1);
        } else if (keyCode == KeyEvent.VK_DOWN) {
            selectedOption = Math.min(1, selectedOption + 1);
        }
    }

    public void resetSelection() {
        selectedOption = 0;
    }

    public int getSelectedOption() {
        return selectedOption;
    }
}