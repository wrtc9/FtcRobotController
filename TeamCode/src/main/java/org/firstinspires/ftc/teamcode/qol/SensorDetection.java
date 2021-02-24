package org.firstinspires.ftc.teamcode.qol;

/**
 * SensorDetection is an enum for sensor detections and sensors in general. Used typically in an
 * EnumSet.
 *
 * @author Will (wrtc9)
 */
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

    public Location getDetour(Location position) {
        float distance = 8f;
        position.setX(position.getX() + x * distance);
        position.setY(position.getY() + y * distance);
        return position;
    }

    public String toString_() {
        return name;
    }
}
