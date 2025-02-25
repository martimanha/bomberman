package bomberman;

public class GameConstants {
    // Configurações de Tela e Mapa
    public static final int TILE_SIZE = 32;
    public static final int MAP_COLS = 30;
    public static final int MAP_ROWS = 20;
    public static final int SCREEN_WIDTH = TILE_SIZE * MAP_COLS;
    public static final int SCREEN_HEIGHT = TILE_SIZE * MAP_ROWS;

    // Configurações do Jogador
    public static final int PLAYER_START_LIVES = 3;
    public static final int PLAYER_MAX_LIVES = 3;
    public static final float PLAYER_BASE_SPEED = 0.2f;
    public static final int PLAYER_INVULNERABILITY_DURATION = 5000; // ms

    // Configurações de Bombas
    public static final int BOMB_BASE_POWER = 1;
    public static final int MAX_BOMB_POWER = 5;
    public static final int BOMB_FUSE_TIME = 2000; // ms

    // Configurações de Explosão
    public static final int EXPLOSION_DURATION = 500; // ms

    // Configurações de Inimigos
    public static final int ENEMY_MOVE_INTERVAL = 1000; // ms
    public static final float ENEMY_BASE_DAMAGE = 1.0f;

    // Configurações de Power-Ups
    public static final float BASE_POWERUP_CHANCE = 0.3f; // 30%
    public static final float POSITIVE_POWERUP_CHANCE = 0.7f; // 70%
    public static final float NEGATIVE_POWERUP_CHANCE = 0.3f; // 30%
    public static final float MIN_LUCK = 0.1f;
    public static final int POWERUP_DURATION = 15000; // 15s

    // Configurações de Velocidade
    public static final int MAX_SPEED_BOOSTS = 3;

    // Configurações do HUD
    public static final int HUD_PADDING_X = 12; // Padding horizontal
    public static final int HUD_PADDING_Y = 12; // Padding vertical
}