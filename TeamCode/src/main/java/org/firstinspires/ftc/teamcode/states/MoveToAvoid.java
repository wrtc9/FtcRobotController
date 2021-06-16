package org.firstinspires.ftc.teamcode.states;

import org.firstinspires.ftc.teamcode.qol.Location;
import org.firstinspires.ftc.teamcode.qol.SensorDetection;
import org.firstinspires.ftc.teamcode.qol.TelemetryInfo;

import java.util.EnumSet;

/**
 * MoveToAvoid runs when the robot hits an obstacle. The class checks which sides are obscured and
 * then moves to the detour which is closest to the ultimate target. The best side to avoid to is
 * recalculated every time another obstacle is found. This class extends {@link MoveWithPID MoveWithPID}.
 * @author Will (wrtc9)
 */
public class MoveToAvoid extends MoveWithPID { // edit this so that the robot doesn't go back and forth; also make this work better with the new Target type

    private final Location target; // this is going to be where MoveWithPID wants to go
    private Location detour; // where MoveToAvoid is headed

    private EnumSet<SensorDetection> previousDetections;
    private SensorDetection bestDirection;

    private final TelemetryInfo avoidanceInfo = new TelemetryInfo("AVOIDING:"),
                                detectionInfo = new TelemetryInfo("DETECTIONS:");

    protected MoveToAvoid(String name, AbState nextState, Location target) {
        super(name, nextState);
        this.target = target;
    } // add target to the constructor

    public AbState next() {
        if (distanceCheck(precision, rotationPrecision)){ // not sure if distance check will work properly
            return nextState;
        }

        else {
            return this;
        }
    }

    @Override
    protected Location getTarget() { // if there is a change in detections, change best direction (we'll have to avoid going back and forth); if there are detections, change target
        EnumSet<SensorDetection> detections = movementHandler.getSensorDetections(sensorPrecision);
        Location currentPosition = new Location(robotX, robotY, robotR);

        EnumSet<SensorDetection> newDetections = detections.clone(); // find rising edge new detections
        newDetections.removeAll(previousDetections);

        if (!newDetections.isEmpty()) { // rising edge only; or if we find a way to avoid doubling back we could do both edges
            EnumSet<SensorDetection> openSides = EnumSet.complementOf(detections); // find where we can avoid to

            SensorDetection leastDirection = null;
            double leastDistance = 0;

            for (SensorDetection direction : openSides) { // find which side is best to detour to
                Location possibleDetour = direction.getDetour(currentPosition);
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

        avoidanceInfo.setContent(bestDirection.toString_());
        String content = ""; // do this to format detections
        for (SensorDetection i : detections){
            content += i.toString_() + ", ";
        }
        content = content.substring(0, content.length()-2); // chops off last ", "
        detectionInfo.setContent(content); // hopefully this will work

        previousDetections = detections;
        return detour;
    }
}
