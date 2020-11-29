package org.firstinspires.ftc.teamcode.qol;

import org.jetbrains.annotations.NotNull;

public enum SensorDetection {
    UP(0, 1f, "UP"),
    DOWN(0, -1f, "DOWN"),
    RIGHT(1f, 0, "RIGHT"),
    LEFT(-1f, 0, "LEFT");

    private final float x;
    private final float y;
    private final String name;

    SensorDetection(float x, float y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public Target getDetour(Target position) {
        float distance = 8f;
        position.setX(position.getX() + x * distance);
        position.setY(position.getY() + y * distance);
        return position;
    }

    @NotNull
    public String toString() {
        return name;
    }
}
