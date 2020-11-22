package org.firstinspires.ftc.teamcode.qol;

public enum SensorDetection {
    UP(0, 1f),
    DOWN(0, -1f),
    RIGHT(1f, 0),
    LEFT(-1f, 0);

    private final float x;
    private final float y;

    SensorDetection(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Target getDetour(Target position) {
        float distance = 8f;
        position.setX(position.getX() + x * distance);
        position.setY(position.getY() + y * distance);
        return position;
    }
}
