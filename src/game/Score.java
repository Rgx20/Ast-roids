package game;

public class Score {

    private static final double REBOOT_TIME = 3;
    private static final int MULTIPLIER_MODIFIER_VALUE = 1;
    private static final int POINTS = 10;

    private double value;
    private int multiplier;
    private double multiplierTime;

    public Score() {
        value = 0;
        multiplier = 1;
    }

    public double getValue() { return value;}
    public int getMultiplier() { return multiplier; }

    public void update(double dt) {
        if (multiplierTime > 0)
            multiplierTime = multiplierTime - dt;
        if((multiplierTime < 0) && (multiplier > 1)){
            multiplier = multiplier - MULTIPLIER_MODIFIER_VALUE;
            putMultiplierTimeBackTo(REBOOT_TIME);
        }
    }

    private void addPoints(double points) { value += points * multiplier; }

    public void notifyAsteroidHit(double points) { addPoints(points); }

    public void addMultiplier(int value) {
        if (getMultiplier() >= 1)
            multiplier = multiplier + value;
    }

    public void putMultiplierTimeBackTo(double dt) {
        multiplierTime = dt;
    }

    public static int getPoints() {
        return POINTS;
    }

    public static double getRebootTime() {
        return REBOOT_TIME;
    }

    public static int getMultiplierModifierValue() {
        return MULTIPLIER_MODIFIER_VALUE;
    }



}
