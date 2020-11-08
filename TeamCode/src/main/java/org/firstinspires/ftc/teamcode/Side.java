package org.firstinspires.ftc.teamcode;

public enum Side {
    BLUE(1),
    RED(-1); // red is just blue mirrored across y axis (-x)

    // don't know if this stuff works, but oh well
    private int sign;

    Side(int sign){
        this.sign = sign;
    }

    public int getSign() {
        return sign;
    }
}
