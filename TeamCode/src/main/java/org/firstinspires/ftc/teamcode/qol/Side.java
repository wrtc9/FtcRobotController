package org.firstinspires.ftc.teamcode.qol;

/**
 * Enum for starting side of robot
 *
 * @author Will (wrtc9)
 */
public enum Side {
    BLUE("Blue"),
    RED("Red"); // red is just blue mirrored across y axis (-x)

    // don't know if this stuff works, but oh well

    private String name;

    Side (String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
