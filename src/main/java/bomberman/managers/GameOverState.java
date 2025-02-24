package bomberman.managers;

import bomberman.rendering.GameOverMenu;

/**
 * Gerencia o estado de game over e a interação com o menu correspondente.
 */
public class GameOverState {
    private boolean isActive;
    private final GameOverMenu menu;

    public GameOverState() {
        this.isActive = false;
        this.menu = new GameOverMenu();
    }

    /**
     * Ativa o estado de game over, exibindo o menu.
     */
    public void activate() {
        isActive = true;
    }

    /**
     * Desativa o estado de game over, ocultando o menu.
     */
    public void deactivate() {
        isActive = false;
        menu.resetSelection(); // Reseta a seleção para a opção padrão
    }

    /**
     * Verifica se o estado de game over está ativo.
     * @return true se o menu de game over está visível.
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Obtém o menu associado ao estado de game over.
     */
    public GameOverMenu getMenu() {
        return menu;
    }
}