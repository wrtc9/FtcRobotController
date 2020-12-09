package org.firstinspires.ftc.teamcode.qol;

import java.util.function.Consumer;

/**
 * The Target Class adds a standardized object for positions on the playing field. As opposed to a
 * float[], Target provides length safety, better readability, and useful functions.
 *
 * @author Will (wrtc9)
 */
public class Target { // just adds a bit of length safety and readability, also can do cool stuff like toMM
    // TODO: Better implement this type
    private float x;
    private float y;
    private float r;

    private float[] iterable;

    public Target (float x, float y, float r) {
        r %= 360; // making sure r is in bounds

        this.x = x;
        this.y = y;
        this.r = r;

        iterable = new float[] {x, y, r};
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getR() {
        return r;
    }

    public void setX(float x) {
        this.x = x;
        iterable[0] = x;
    }

    public void setY(float y) {
        this.y = y;
        iterable[1] = y;
    }

    public void setR(float r) {
        this.r = r;
        iterable[2] = r;
    }

    public float[] getIterable() {
        return iterable;
    }

    /**
     * Multiplies the x and y components of the Target by 25.4
     *
     * @author Will (wrtc9)
     */
    public void toMM () {
        float mmPerIn = 25.4f;
        x *= mmPerIn;
        y *= mmPerIn;
    }

    /**
     * Typically used when changing sides
     * @return A target which is mirrored across the y-axis of the playing field.
     *
     * @author Will (wrtc9)
     */
    public Target getMirroredTarget() { // idk about this, this should only be done on rising edge; or else it could return the formatted target w/o changing target
        float formattedX = x;
        float formattedR = r;

        formattedX *= -1;
        formattedR = (180 - formattedR) % 360;

        return new Target(formattedX, y, formattedR);
    }

    public Target clone() {
        return new Target(x, y, r);
    }
}
