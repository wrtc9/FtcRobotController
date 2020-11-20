package org.firstinspires.ftc.teamcode.qol;

public class Target { // just adds a bit of length safety and readability, also can do cool stuff like multiplyBy
    private final float[] target; // I want this to be immutable since that's the sorta way the states handle switching targets

    public Target (float x, float y, float r) {
        target = new float[] {x, y, r};
    }

    public float getX() {
        return target[0];
    }

    public float getY() {
        return target[1];
    }

    public float getR() {
        return target[2];
    }

    public float[] getIterable() {
        return target;
    }

    public void multiplyBy (float coeff) {
        for (int i = 0; i < 3; i++) {
            target[i] *= coeff;
        }
    }
}
