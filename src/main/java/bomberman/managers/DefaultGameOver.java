package bomberman.managers;

/**
 * Implementação padrão das ações de game over.
 */
public class DefaultGameOver implements GameOver {
    private final GameResetHandler resetHandler;

    public DefaultGameOver(GameResetHandler resetHandler) {
        this.resetHandler = resetHandler;
    }

    @Override
    public void execute(int selectedOption) {
        switch (selectedOption) {
            case 0:
                resetHandler.onGameReset(); // Reinicia o jogo
                break;
            case 1:
                System.exit(0); // Encerra a aplicação
                break;
            default:
                throw new IllegalArgumentException("Opção inválida: " + selectedOption);
        }
    }
}