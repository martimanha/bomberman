package bomberman.managers;

public class TimerManager {
    private long startTime;
    private int duration;

    public TimerManager(int duration) {
        this.duration = duration;
        reset();
    }

    public void reset() {
        startTime = System.currentTimeMillis();
    }

    public int getRemainingTime() {
        long elapsed = (System.currentTimeMillis() - startTime) / 1000;
        int remainingTime = duration - (int) elapsed;
        return Math.max(remainingTime, 0);
    }

    public boolean isTimeUp() {
        return getRemainingTime() <= 0;
    }
}