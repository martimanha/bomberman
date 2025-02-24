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
        // Carregar fonte personalizada
        try {
            menuFont = Font.createFont(Font.TRUETYPE_FONT,
                            getClass().getResourceAsStream("/fonts/FredokaOne-Regular.ttf"))
                    .deriveFont(Font.BOLD, 24f);
        } catch (Exception e) {
            menuFont = new Font("Arial", Font.BOLD, 24);
        }
    }

    /**
     * Processa cliques do mouse no menu.
     */
    public void handleMouseClick(MouseEvent e) {
        int mouseY = e.getY();
        int menuY = e.getComponent().getHeight() / 2;

        if (mouseY >= menuY && mouseY <= menuY + OPTION_HEIGHT) {
            selectedOption = 0; // "Tentar Novamente"
        } else if (mouseY >= menuY + OPTION_HEIGHT && mouseY <= menuY + 2 * OPTION_HEIGHT) {
            selectedOption = 1; // "Sair"
        }
    }

    /**
     * Processa teclas pressionadas (↑/↓) para navegar no menu.
     */
    public void handleKeyInput(int keyCode) {
        if (keyCode == KeyEvent.VK_UP) {
            selectedOption = Math.max(0, selectedOption - 1);
        } else if (keyCode == KeyEvent.VK_DOWN) {
            selectedOption = Math.min(1, selectedOption + 1);
        }
    }

    /**
     * Renderiza o menu de game over.
     */
    public void render(Graphics2D g2, int screenWidth, int screenHeight) {
        // Fundo semi-transparente
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, screenWidth, screenHeight);

        // Título
        g2.setFont(menuFont.deriveFont(32f));
        HUDRenderer.drawTextWithOutline(g2, "FIM DO JOGO",
                screenWidth/2 - 100,
                screenHeight/2 - 40,
                Color.RED);

        // Opções do menu
        String[] options = {"Tentar Novamente", "Sair"};
        g2.setFont(menuFont);
        for (int i = 0; i < options.length; i++) {
            Color color = (i == selectedOption) ? Color.YELLOW : Color.WHITE;
            HUDRenderer.drawTextWithOutline(g2, options[i],
                    screenWidth/2 - MENU_WIDTH/2,
                    screenHeight/2 + i * OPTION_HEIGHT,
                    color);
        }
    }

    /**
     * Reseta a seleção para a opção padrão (0).
     */
    public void resetSelection() {
        selectedOption = 0;
    }

    /**
     * Obtém a opção atualmente selecionada.
     */
    public int getSelectedOption() {
        return selectedOption;
    }
}