package org.firstinspires.ftc.teamcode.states;

import org.firstinspires.ftc.teamcode.qol.SensorDetection;
import org.firstinspires.ftc.teamcode.qol.Target;
import org.firstinspires.ftc.teamcode.qol.TelemetryInfo;

import java.util.EnumSet;

public class MoveToAvoid extends MoveWithPID { // edit this so that the robot doesn't go back and forth; also make this work better with the new Target type

    private final Target target; // this is going to be where MoveWithPID wants to go
    private Target detour; // where MoveToAvoid is headed

    @Deprecated
    private float[][] visitedLocations; // for checking if we're looping

    private EnumSet<SensorDetection> previousDetections;
    private SensorDetection bestDirection;

    private final TelemetryInfo avoidanceInfo = new TelemetryInfo("AVOIDING:"),
                                detectionInfo = new TelemetryInfo("DETECTIONS:");

    protected MoveToAvoid(String name, AbState nextState, Target target) {
        super(name, nextState);
        this.target = target;
    } // add target to the constructor

    public AbState next() {
        if (distanceCheck(precision)){ // not sure if distance check will work properly
            return nextState;
        }

        else {
            return this;
        }
    }

    @Override
    protected Target getTarget() { // if there is a change in detections, change best direction (we'll have to avoid going back and forth); if there are detections, change target
        EnumSet<SensorDetection> detections = movementHandler.getSensorDetections(sensorPrecision);
        Target currentPosition = new Target(robotX, robotY, robotR);

        EnumSet<SensorDetection> newDetections = detections.clone(); // find rising edge new detections
        newDetections.removeAll(previousDetections);

        if (!newDetections.isEmpty()) { // rising edge only; or if we find a way to avoid doubling back we could do both edges
            EnumSet<SensorDetection> openSides = EnumSet.complementOf(detections); // find where we can avoid to

            SensorDetection leastDirection = null;
            double leastDistance = 0;

            for (SensorDetection direction : openSides) { // find which side is best to detour to
                Target possibleDetour = direction.getDetour(currentPosition);
                double distance = movementHandler.distanceTo(target.getX() - possibleDetour.getX(), target.getY() - possibleDetour.getY()); // finds closest possible detour to the ultimate target

                if (distance < leastDistance || leastDirection == null) { // this is a bit worrisome
                    leastDirection = direction;
                    leastDistance = distance;
                }
            }

            bestDirection = leastDirection; // save the best direction
        }

        if (!detections.isEmpty()) { // if there are still blocked sides, move to the side which is closest to the target
            detour = bestDirection.getDetour(currentPosition);
        }

        avoidanceInfo.setContent(bestDirection.toString());
        detectionInfo.setContent(detections.toString()); // hopefully this will work

        previousDetections = detections;
        return detour;
    }
}
