package org.firstinspires.ftc.teamcode;

public enum WobbleSetting {
    C(Locations.C, 4), // defaults for blue side, change x and r for red
    B(Locations.B, 1),
    A(Locations.A, 0);

    private Locations location;
    private int stackHeight;

    WobbleSetting(Locations location, int stackHeight) { // uncomment when you find out drop off locations
        this.location = location;
        this.stackHeight = stackHeight;
    }

    public float[] getTarget(Side side) {
        return location.getLocation(side);
    }

    public int getStackHeight() { // streamline this?
        return stackHeight;
    }
}
