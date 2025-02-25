package bomberman.rendering;

import bomberman.entities.Player;
import bomberman.managers.StatusManager;
import bomberman.managers.TimerManager;
import java.awt.*;

import static bomberman.GameConstants.*;

public class HUDRenderer {
    private static Font hudFont;
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color OUTLINE_COLOR = new Color(0x1A1A1A);
    private static final int OUTLINE_THICKNESS = 2;

    static {
        try {
            hudFont = Font.createFont(Font.TRUETYPE_FONT,
                            HUDRenderer.class.getResourceAsStream("/fonts/FredokaOne-Regular.ttf"))
                    .deriveFont(Font.BOLD, 20f);
        } catch (Exception e) {
            hudFont = new Font("Arial", Font.BOLD, 20);
        }
    }

    public static void render(Graphics2D g2, StatusManager statusManager, Player player, TimerManager timerManager) {
        renderTopHUD(g2, statusManager, player);
        renderBottomHUD(g2, statusManager);
        renderTimer(g2, timerManager);
    }

    private static void renderTopHUD(Graphics2D g2, StatusManager statusManager, Player player) {
        g2.setFont(hudFont.deriveFont(20f));
        renderLives(g2, statusManager, HUD_PADDING_Y + 20);
        renderBombCooldown(g2, player, HUD_PADDING_Y + 20 + g2.getFontMetrics().getHeight());
    }

    private static void renderBottomHUD(Graphics2D g2, StatusManager statusManager) {
        g2.setFont(hudFont.deriveFont(16f));
        int lineHeight = g2.getFontMetrics().getHeight();
        int startY = SCREEN_HEIGHT - HUD_PADDING_Y - lineHeight * 4;

        renderBombPower(g2, statusManager, startY);
        renderSpeed(g2, statusManager, startY + lineHeight);
        renderLuck(g2, statusManager, startY + lineHeight * 2);
        renderDamage(g2, statusManager, startY + lineHeight * 3);
    }

    private static void renderTimer(Graphics2D g2, TimerManager timerManager) {
        int time = timerManager.getRemainingTime();
        Color timerColor = (time <= 30) ? Color.RED : TEXT_COLOR; // Vermelho se ≤30s

        g2.setFont(hudFont.deriveFont(20f));
        String timerText = String.format("Tempo: %02d:%02d", time / 60, time % 60);
        drawTextWithOutline(g2, timerText, TIMER_X, TIMER_Y, timerColor);
    }

    public static void drawTextWithOutline(Graphics2D g2, String text, int x, int y, Color mainColor) {
        // Contorno
        g2.setColor(OUTLINE_COLOR);
        for (int dx = -OUTLINE_THICKNESS; dx <= OUTLINE_THICKNESS; dx++) {
            for (int dy = -OUTLINE_THICKNESS; dy <= OUTLINE_THICKNESS; dy++) {
                if (dx != 0 || dy != 0) {
                    g2.drawString(text, x + dx, y + dy);
                }
            }
        }

        // Texto principal
        g2.setColor(mainColor);
        g2.drawString(text, x, y);
    }

    private static void renderLives(Graphics2D g2, StatusManager status, int y) {
        String text = "Vidas: " + status.getLives();
        drawTextWithOutline(g2, text, HUD_PADDING_X, y, TEXT_COLOR);
    }

    private static void renderBombCooldown(Graphics2D g2, Player player, int y) {
        long cooldown = 3000 - (System.currentTimeMillis() - player.getLastBombTime());
        if (cooldown > 0) {
            String text = "Recarga: " + (cooldown / 1000) + "s";
            drawTextWithOutline(g2, text, HUD_PADDING_X, y, Color.ORANGE);
        }
    }

    private static void renderBombPower(Graphics2D g2, StatusManager status, int y) {
        String text = "Poder: " + status.getBombPower();
        drawTextWithOutline(g2, text, HUD_PADDING_X, y, TEXT_COLOR);
    }

    private static void renderSpeed(Graphics2D g2, StatusManager status, int y) {
        int speedPercent = (int)(status.getSpeedMultiplier() * 100);
        String text = "Velocidade: " + speedPercent + "%";
        drawTextWithOutline(g2, text, HUD_PADDING_X, y, TEXT_COLOR);
    }

    private static void renderLuck(Graphics2D g2, StatusManager status, int y) {
        int luckPercent = (int)(status.getLuckMultiplier() * 100);
        String text = "Sorte: " + luckPercent + "%";
        drawTextWithOutline(g2, text, HUD_PADDING_X, y, TEXT_COLOR);
    }

    private static void renderDamage(Graphics2D g2, StatusManager status, int y) {
        int damagePercent = (int)(status.getEnemyDamageMultiplier() * 100);
        String text = "Dano: " + damagePercent + "%";
        drawTextWithOutline(g2, text, HUD_PADDING_X, y, TEXT_COLOR);
    }
}