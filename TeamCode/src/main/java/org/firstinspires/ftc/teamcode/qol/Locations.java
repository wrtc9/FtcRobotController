package org.firstinspires.ftc.teamcode.qol;

/**
 * Locations is an enum that stores useful locations of the playing field.
 *
 * @author Will (wrtc9)
 */
public enum Locations {
    ORIGIN(new Location(0f, 0f, 0f)),
    A(new Location(-60.1875f, -1.25f, 90f)),
    B(new Location(-36.625f, 22.625f, 90f)),
    C(new Location(-60.1875f, 46.372f, 90f)),
    RING_STACK(new Location(-38f, -42.75f, 180f)),
    POWER_SHOT_LINE(new Location(-13.625f, 8.25f, 270f));

    private final Location location;

    Locations (Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
