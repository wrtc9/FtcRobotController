package org.firstinspires.ftc.teamcode.qol;

public enum Locations {
    ORIGIN(new float[]{0f, 0f, 0f}),
    A(new float[] {-60.1875f, -1.25f, 90f}),
    B(new float[] {-36.625f, 22.625f, 90f}),
    C(new float[] {-60.1875f, 46.372f, 90f}),
    RING_STACK(new float[] {-38f, -42.75f, 180f}),
    POWER_SHOT_LINE(new float[]{-13.625f, 8.25f, 270f});

    private final float[] location;
    private final float mmPerIn = 25.4f;

    Locations (float[] location) {
        this.location = location;
        for (int i = 0; i < 2; i++) {
            location[i] *= mmPerIn;
        }
    }

    public float[] getLocation(Side side) {
        float[] realLocation = location.clone();
        realLocation[0] *= side.getSign();
        if (side == Side.RED) {
            float complement = (180 - realLocation[2]);
            realLocation[2] = (complement > 0) ? complement : 360 + complement;
        }
        return realLocation;
    }
}
