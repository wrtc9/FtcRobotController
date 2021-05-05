package org.firstinspires.ftc.teamcode.handlers;

import org.firstinspires.ftc.teamcode.qol.Location;
import org.firstinspires.ftc.teamcode.qol.TelemetryInfo;

import java.util.ArrayList;

public class PositionEstimator {
    private MovementHandler movementHandler;
    private int[] previousTicks;
    private int[] currentTicks;
    private final int TICKS_PER_REVOLUTION = 1440;
    private final double WHEEL_CIRCUMFERENCE_MM = 2.0 * 25.4 * 2.0 * Math.PI; // 7, 16
    private final double MM_PER_TICK = WHEEL_CIRCUMFERENCE_MM / TICKS_PER_REVOLUTION;
    private final double WHEEL_DISTANCE_Y = 7 * 25.4;
    private final double WHEEL_DISTANCE_X = 16 * 25.4;
    private final double ROTATION_DIAMETER = Math.sqrt(Math.pow(WHEEL_DISTANCE_X, 2) + Math.pow(WHEEL_DISTANCE_Y, 2));
    private final double ROTATION_CIRCUMFERENCE = ROTATION_DIAMETER * Math.PI;
    private final double DEGREE_PER_TICK = MM_PER_TICK / (ROTATION_CIRCUMFERENCE/360);

    private ArrayList<TelemetryInfo> telemetryInfos = new ArrayList<>();
    private TelemetryInfo linTele = new TelemetryInfo("Linear Estimation");
    private TelemetryInfo latTele = new TelemetryInfo("Lateral Estimation");
    private TelemetryInfo rotTele = new TelemetryInfo("Rotational Estimation");

    public PositionEstimator(MovementHandler movementHandler){
        this.movementHandler = movementHandler;

        telemetryInfos.add(linTele);
        telemetryInfos.add(latTele);
        telemetryInfos.add(rotTele);
    }

    public Location update(Location position) {
        if (previousTicks == null) {
            previousTicks = movementHandler.getTicks();
            return position;
        }
        else {
            currentTicks = movementHandler.getTicks();
            int[] deltaTicks = new int[4];
            for (int i = 0; i < 4; i++) {
                int temp = currentTicks[i] - previousTicks[i];
                deltaTicks[i] = (Math.abs(temp) < TICKS_PER_REVOLUTION/2) ? temp : temp - TICKS_PER_REVOLUTION * Integer.signum(temp); // if motor makes a full revolution
            }
            int linTicks = (deltaTicks[0] + deltaTicks[1] + deltaTicks[2] + deltaTicks[3])/4;
            int latTicks = (-deltaTicks[0] + deltaTicks[1] + deltaTicks[2] - deltaTicks[3])/4;
            int rotTicks = (-deltaTicks[0] + deltaTicks[1] - deltaTicks[2] + deltaTicks[3])/4;

            double linMM = linTicks * MM_PER_TICK;
            double latMM = latTicks * MM_PER_TICK;
            double rotDegrees = rotTicks * DEGREE_PER_TICK;

            previousTicks = currentTicks;

            linTele.setContent(String.valueOf(linMM));
            latTele.setContent(String.valueOf(latMM));
            rotTele.setContent(String.valueOf(rotDegrees));

            return new Location((float) (latMM + position.getX()), (float) (linMM + position.getY()), (float) (rotDegrees + position.getR()));
        }
    }

    public void reset() {
        previousTicks = movementHandler.getTicks();
    }

    public ArrayList<TelemetryInfo> getTelemetry() {
        return telemetryInfos;
    }
}
