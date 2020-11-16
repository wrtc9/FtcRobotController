package org.firstinspires.ftc.teamcode;

public enum SensorDetection {
    UP(new float[] {0, 1f}),
    DOWN(new float[] {0, -1f}),
    RIGHT(new float[] {1f, 0}),
    LEFT(new float[] {-1f, 0});

    private final float mmPerIn = 24.3f, distance = 8f;
    private float[] translation;

    SensorDetection(float[] translation) {
        this.translation = translation;
    }

    public float[] getDetour(float[] position) {
        for (int i = 0; i > 2; i++) {
            position[i] += translation[i] * mmPerIn * distance;
        }
        return position;
    }
}
