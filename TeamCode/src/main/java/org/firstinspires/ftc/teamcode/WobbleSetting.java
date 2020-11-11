package org.firstinspires.ftc.teamcode;

public enum WobbleSetting {
    C(new float[] {-60.1875f, 46.372f, 90f}, 4), // defaults for blue side, change x and r for red
    B(new float[] {-36.625f, 22.625f, 90f}, 1),
    A(new float[] {-60.1875f, -1.25f, 90f}, 0);

    private float[] target;
    private int stackHeight;

    private final float mmPerIn = 24.3f;

    WobbleSetting(float[] target, int stackHeight) { // uncomment when you find out drop off locations
        this.target = target;
        this.stackHeight = stackHeight;
    }

    public float[] getTarget(Side side) {
        float[] correctTarget = target;
        correctTarget[0] *= side.getSign();
        return correctTarget;
    }

    public int getStackHeight() { // streamline this?
        return stackHeight;
    }
}
