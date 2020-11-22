package org.firstinspires.ftc.teamcode.qol;

public enum Locations {
    ORIGIN(new Target(0f, 0f, 0f)),
    A(new Target(-60.1875f, -1.25f, 90f)),
    B(new Target(-36.625f, 22.625f, 90f)),
    C(new Target(-60.1875f, 46.372f, 90f)),
    RING_STACK(new Target(-38f, -42.75f, 180f)),
    POWER_SHOT_LINE(new Target(-13.625f, 8.25f, 270f));

    private final Target location;

    Locations (Target location) {
        this.location = location;
    }

    public Target getLocation() {
        return location;
    }
}
