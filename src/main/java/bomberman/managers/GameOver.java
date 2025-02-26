package bomberman.managers;

public interface GameOver {
    void execute(int selectedOption);

    static GameOver createDefault(GameResetHandler resetHandler) {
        return new DefaultGameOver(resetHandler);
    }
}