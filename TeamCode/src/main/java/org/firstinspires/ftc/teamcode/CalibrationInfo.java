package org.firstinspires.ftc.teamcode;

import java.io.Serializable;

public class CalibrationInfo implements Serializable {
    private double distance = 0;
    private int ticks = 0;

    public double getTicksPerMM(){
        return ticks / distance;
    }

    public void addDistance(double distance) {
        this.distance = this.distance + distance;
    }

    public void addTicks(int ticks){
        this.ticks = this.ticks + ticks;
    }
}
