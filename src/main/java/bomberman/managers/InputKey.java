package bomberman.managers;

import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.util.HashSet;
import java.util.Set;

public class InputKey extends KeyAdapter {
    private final Set<Integer> pressedKeys = new HashSet<>();
    private int lastKeyPressed = -1;

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (!pressedKeys.contains(keyCode)) {
            pressedKeys.add(keyCode);
            lastKeyPressed = keyCode;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        pressedKeys.remove(keyCode);
        if (lastKeyPressed == keyCode) {
            lastKeyPressed = -1;
        }
    }

    public boolean isKeyPressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }

    public int getLastKeyPressed() {
        return lastKeyPressed;
    }

    public void clearLastKeyPressed() {
        lastKeyPressed = -1;
    }

    public Set<Integer> getActiveKeys() {
        return new HashSet<>(pressedKeys);
    }
}