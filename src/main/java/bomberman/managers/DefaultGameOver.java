package bomberman.managers;

public class DefaultGameOver implements GameOver {
    private final GameResetHandler resetHandler;

    public DefaultGameOver(GameResetHandler resetHandler) {
        this.resetHandler = resetHandler;
    }

    @Override
    public void execute(int selectedOption) {
        switch (selectedOption) {
            case 0:
                resetHandler.onGameReset();
                break;
            case 1:
                System.exit(0);
                break;
            default:
                throw new IllegalArgumentException("Opção inválida: " + selectedOption);
        }
    }
}