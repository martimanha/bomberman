package bomberman;

public class GameConstants {
    public static final int TILE_SIZE = 32;
    public static final int MAP_COLS = 30;
    public static final int MAP_ROWS = 20;
    public static final int SCREEN_WIDTH = TILE_SIZE * MAP_COLS;
    public static final int SCREEN_HEIGHT = TILE_SIZE * MAP_ROWS;

    public static final int PLAYER_START_LIVES = 3;
    public static final int PLAYER_MAX_LIVES = 3;
    public static final float PLAYER_BASE_SPEED = 0.2f;
    public static final int PLAYER_INVULNERABILITY_DURATION = 2500;

    public static final int BOMB_BASE_POWER = 1;
    public static final int MAX_BOMB_POWER = 7;
    public static final int BOMB_FUSE_TIME = 2000;

    public static final int EXPLOSION_DURATION = 500;

    public static final int ENEMY_MOVE_INTERVAL = 1000;
    public static final float ENEMY_BASE_SPEED = 0.1f;

    public static final float BASE_POWERUP_CHANCE = 0.3f;
    public static final int POWERUP_DURATION = 15000;

    public static final int MAX_SPEED_BOOSTS = 3;

    public static final int HUD_PADDING_X = 12;
    public static final int HUD_PADDING_Y = 12;
    public static final int TIMER_X = SCREEN_WIDTH - 150;
    public static final int TIMER_Y = HUD_PADDING_Y + 20;

    public static final int LEVEL_TIME = 180;
}