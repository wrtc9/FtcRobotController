package org.firstinspires.ftc.teamcode.states;

import org.firstinspires.ftc.teamcode.qol.SensorDetection;

import java.util.EnumSet;

public class MoveToAvoid extends MoveWithPID { // edit this so that the robot doesn't go back and forth

    private float[] target;

    @Deprecated
    private float[][] visitedLocations; // for checking if we're looping

    private EnumSet<SensorDetection> previousDetections;
    private SensorDetection bestDirection;

    protected MoveToAvoid(String name, AbState nextState) {
        super(name, nextState);
    }

    public AbState next() {
        if ((target[0] - robotX) < precision && (target[1] - robotY) < precision && (target[2] - robotR) < precision){
            nextState.init(this);
            return nextState;
        }

        else {
            return this;
        }
    }

    @Override
    protected float[] getTarget() { // if there is a change in detections, change best direction (we'll have to avoid going back and forth); if there are detections, change target
        EnumSet<SensorDetection> detections = movementHandler.getSensorDetections(sensorPrecision);
        float[] currentPosition = new float[]{robotX, robotY, robotR};

        EnumSet<SensorDetection> newDetections = detections.clone();
        newDetections.removeAll(previousDetections);

        if (!newDetections.isEmpty()) { // rising edge only
            EnumSet<SensorDetection> openSides = EnumSet.complementOf(detections); // find where we can avoid to

            SensorDetection leastDirection = null;
            double leastDistance = 0;

            for (SensorDetection direction : openSides) { // find which side is best to detour to
                float[] detour = direction.getDetour(currentPosition);
                double distance = movementHandler.distanceTo(target[0] - detour[0], target[1] - detour[1]);

                if (distance < leastDistance || leastDirection == null) { // this is a bit worrisome
                    leastDirection = direction;
                    leastDistance = distance;
                }
            }

            bestDirection = leastDirection; // save the best direction

            previousDetections = detections;
        }

        if (!detections.isEmpty()) {
            target = bestDirection.getDetour(currentPosition);
        }

        return target;
    }
}
