package bomberman.ui;

import static bomberman.GameConstants.SCREEN_HEIGHT;
import static bomberman.GameConstants.SCREEN_WIDTH;
import bomberman.utils.UILoader;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class GameSelectionMenuState {
    private BufferedImage background;
    private BufferedImage title;
    private List<ModeButton> buttons;
    private Button playButton;
    private Button backButton;
    private boolean isActive;
    private MainMenuState mainMenuState;

    // Configurações de layout
    private static final int BUTTON_START_X = 50;
    private static final int BUTTON_SPACING_Y = 100;
    private static final int DESCRIPTIONS_X = 350;
    private static final int DESCRIPTIONS_Y = 115;
    private static final int RIGHT_PADDING = 50;
    private static final int ACTION_BUTTON_SPACING = 30;
    private static final int TITLE_X = 50;
    private static final int TITLE_Y = 50;

    public GameSelectionMenuState(MainMenuState mainMenuState) {
        this.mainMenuState = mainMenuState;
        loadResources();
        initializeButtons();
        isActive = false;
    }

    private void loadResources() {
        background = UILoader.loadUISprite("main_menu/background.png");
        title = UILoader.loadUISprite("gameSelection_menu/title.png");
    }

    private void initializeButtons() {
        buttons = new ArrayList<>();
        int yPos = 150;

        // Botão Normal selecionado por padrão
        ModeButton normalButton = new ModeButton("Normal", BUTTON_START_X, yPos);
        normalButton.setSelected(true);
        buttons.add(normalButton);
        yPos += BUTTON_SPACING_Y;

        // Nova ordem
        buttons.add(new ModeButton("Labirinto", BUTTON_START_X, yPos));
        yPos += BUTTON_SPACING_Y;
        buttons.add(new ModeButton("Temporizado", BUTTON_START_X, yPos));
        yPos += BUTTON_SPACING_Y;
        buttons.add(new ModeButton("Difícil", BUTTON_START_X, yPos));

        // Botões de ação no canto inferior direito
        int playWidth = UILoader.loadUISprite("gameSelection_menu/play.png").getWidth();
        int backWidth = UILoader.loadUISprite("gameSelection_menu/back.png").getWidth();

        int backX = SCREEN_WIDTH - RIGHT_PADDING - backWidth - ACTION_BUTTON_SPACING - playWidth;
        int playX = SCREEN_WIDTH - RIGHT_PADDING - backWidth;
        int actionY = SCREEN_HEIGHT - 100;

        playButton = new Button(
                "gameSelection_menu/play.png",
                "gameSelection_menu/play_hover.png",
                playX,
                actionY
        );

        backButton = new Button(
                "gameSelection_menu/back.png",
                "gameSelection_menu/back_hover.png",
                backX,
                actionY
        );
    }

    public void handleMouseMove(Point mousePosition) {
        buttons.forEach(btn -> btn.update(mousePosition));
        playButton.update(mousePosition);
        backButton.update(mousePosition);
    }

    public void handleClick(MouseEvent e) {
        handleModeSelection(e);
        handleActionButtons(e);
    }

    private void handleModeSelection(MouseEvent e) {
        buttons.forEach(btn -> {
            if(btn.isClicked(e)) {
                buttons.forEach(b -> b.setSelected(false));
                btn.setSelected(true);
            }
        });
    }

    private void handleActionButtons(MouseEvent e) {
        if(playButton.isClicked(e)) {
            startGame();
        }
        else if(backButton.isClicked(e)) {
            returnToMainMenu();
        }
    }

    private void startGame() {
        isActive = false;
    }

    private void returnToMainMenu() {
        isActive = false;
        mainMenuState.setActive(true);
    }

    public String getSelectedMode() {
        return buttons.stream()
                .filter(ModeButton::isSelected)
                .findFirst()
                .map(ModeButton::getModeName)
                .orElse("Normal");
    }

    public void render(Graphics2D g2) {
        renderBackground(g2);
        renderTitle(g2);
        renderModeButtons(g2);
        renderActionButtons(g2);
    }

    private void renderBackground(Graphics2D g2) {
        g2.drawImage(background, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
    }

    private void renderTitle(Graphics2D g2) {
        if(title != null) {
            g2.drawImage(title, TITLE_X, TITLE_Y, null);
        }
    }

    private void renderModeButtons(Graphics2D g2) {
        buttons.forEach(btn -> {
            btn.render(g2);
            if(btn.isHovered() || btn.isSelected()) { // Mostra descrição se selecionado
                renderModeDescription(g2, btn);
            }
        });
    }

    private void renderActionButtons(Graphics2D g2) {
        playButton.render(g2);
        backButton.render(g2);
    }

    private void renderModeDescription(Graphics2D g2, ModeButton btn) {
        BufferedImage desc = UILoader.loadUISprite(
                "gameSelection_menu/description_" + btn.getModeName().toLowerCase() + ".png"
        );

        if(desc != null) {
            g2.drawImage(desc, DESCRIPTIONS_X, DESCRIPTIONS_Y, 550, 404, null);
        }
    }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    private static class ModeButton extends Button {
        private final String modeName;
        private boolean selected;
        private final BufferedImage selectedImage;
        private final BufferedImage selectedHoverImage;

        public ModeButton(String modeName, int x, int y) {
            super(
                    "gameSelection_menu/" + modeName.toLowerCase() + ".png",
                    "gameSelection_menu/" + modeName.toLowerCase() + "_hover.png",
                    x, y
            );
            this.modeName = modeName;
            this.selected = false;
            this.selectedImage = UILoader.loadUISprite(
                    "gameSelection_menu/" + modeName.toLowerCase() + "_selected.png"
            );
            this.selectedHoverImage = UILoader.loadUISprite(
                    "gameSelection_menu/" + modeName.toLowerCase() + "_selected_hover.png"
            );
        }

        @Override
        public void render(Graphics2D g2) {
            BufferedImage img = selected ?
                    (isHovered() ? selectedHoverImage : selectedImage) :
                    (isHovered() ? hoverImage : normalImage);

            g2.drawImage(img, getX(), getY(), null);
        }

        public String getModeName() { return modeName; }
        public boolean isSelected() { return selected; }
        public void setSelected(boolean selected) { this.selected = selected; }
    }
}