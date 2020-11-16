package org.firstinspires.ftc.teamcode.depricated;

import org.firstinspires.ftc.teamcode.AbState;
import org.firstinspires.ftc.teamcode.MovementHandler;
import org.firstinspires.ftc.teamcode.SensorDetection;
import org.firstinspires.ftc.teamcode.VuforiaHandler;
import org.firstinspires.ftc.teamcode.states.MoveWithPID;

import java.util.EnumSet;

@Deprecated
public class MoveAndAvoid extends MoveWithPID {
    private EnumSet<SensorDetection> avoidedSides;

    private float[] detour;

    MoveAndAvoid(String name, VuforiaHandler vuforiaHandler, MovementHandler movementHandler, float[] target, AbState nextState, EnumSet<SensorDetection> avoidedSides) {
        super(name, vuforiaHandler, movementHandler, target, nextState);
        this.avoidedSides = avoidedSides;
    }

    public AbState next() { // CHECK THIS TOMORROW

        // avoidance should be done here
        EnumSet<SensorDetection> detections = movementHandler.getSensorDetections(10f); // use complement of
        detections.removeAll(avoidedSides);

        if ((target[0] - robotX) < PRECISION && (target[1] - robotY) < PRECISION && (target[2] - robotR) < PRECISION){
            nextState.init(this);
            return nextState;
        }

        else if (!detections.isEmpty()) { // if there is an obstacle
            avoidedSides.addAll(detections);
            return new MoveAndAvoid("Detour" + name, vuforiaHandler, movementHandler, target, nextState, avoidedSides);
        }

        else {
            return this;
        }
    }

    @Override
    public float[] getTarget() {
        EnumSet<SensorDetection> detections = movementHandler.getSensorDetections(10f); // use complement of
        float[] currentPosition = new float[] {robotX, robotY, robotR};

        if (detections == avoidedSides) { // if there is an obstacle
            EnumSet<SensorDetection> directions = EnumSet.complementOf(detections); // find where we can avoid to

            float[] leastDetour = new float[3];
            double leastDistance = 0;

            for (SensorDetection direction : directions) { // find detour which is closest to target
                float[] detour = direction.getDetour(currentPosition);
                double distance = movementHandler.distanceTo(target[0] - detour[0], target[1] - detour[1]);

                if (distance < leastDistance || leastDistance == 0) {
                    leastDistance = distance;
                    leastDetour = detour;
                }
            }

            detour = leastDetour; // this is so we can store leastDetour
        }

        return detour;
    }

    // add getTarget to MoveWithPID so we don't have to change the whole run method
    // finding the optimal detour will occur in getTarget, constructor will have avoidedSides
}
