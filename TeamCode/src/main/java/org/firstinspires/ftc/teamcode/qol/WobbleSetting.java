package org.firstinspires.ftc.teamcode.qol;

/**
 * The WobbleSetting enum correlates stack height to the specified wobble zone.
 *
 * @author Will (wrtc9)
 */
public enum WobbleSetting {
    C(Locations.C, 4), // defaults for blue side, change x and r for red
    B(Locations.B, 1),
    A(Locations.A, 0);

    private final Locations location;
    private final int stackHeight;

    WobbleSetting(Locations location, int stackHeight) { // uncomment when you find out drop off locations
        this.location = location;
        this.stackHeight = stackHeight;
    }

    public Location getTarget() {
        return location.getLocation();
    }

    public int getStackHeight() { // streamline this?
        return stackHeight;
    }
}
