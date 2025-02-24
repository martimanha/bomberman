package bomberman.managers;

public interface GameOver {
    /**
     * Executa a ação correspondente à opção selecionada no menu.
     * @param selectedOption Índice da opção (0 = Retry, 1 = Exit).
     */
    void execute(int selectedOption);

    /**
     * Cria uma instância padrão com diálogo de confirmação.
     * @param resetHandler Callback para resetar o jogo.
     */
    static GameOver createDefault(GameResetHandler resetHandler) {
        return new DefaultGameOver(resetHandler);
    }
}