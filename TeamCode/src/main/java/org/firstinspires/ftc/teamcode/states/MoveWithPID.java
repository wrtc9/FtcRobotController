package org.firstinspires.ftc.teamcode.states;

import org.firstinspires.ftc.teamcode.MovementHandler;
import org.firstinspires.ftc.teamcode.PID;
import org.firstinspires.ftc.teamcode.SensorDetection;
import org.firstinspires.ftc.teamcode.VuforiaHandler;
import org.firstinspires.ftc.teamcode.AbState;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;

// maybe change this to use inheritance
public class MoveWithPID extends AbState { // this state will forever move closer to the desired target; add tolerance and switch
    // TODO: Clean this up and check

    protected VuforiaHandler vuforiaHandler;
    protected MovementHandler movementHandler;

    protected float[] target, actualTarget; // target must be in mm

    private float[] currentPosition;

    protected AbState nextState;

    private EnumSet<SensorDetection> avoidedSides;

    protected float robotX, robotY, robotR;

    private PID linPID, latPID, rotPID;
    private float linIn, latIn, rotIn;

    protected final float PRECISION = 1f, SENSOR_PRECISION = 10f;

    private final float mmPerIn = 24.3f;

    private EnumSet<SensorDetection> detections;

    private SensorDetection bestDirection;

    protected MoveWithPID(String name, VuforiaHandler vuforiaHandler, MovementHandler movementHandler, float[] target, AbState nextState) {
        super(name);

        this.vuforiaHandler = vuforiaHandler;
        this.movementHandler = movementHandler;

        this.target = target; // should be in mm (x, y, r)
        target[0] *= mmPerIn;
        target[1] *= mmPerIn;

        this.nextState = nextState;
    }

    // this is only used for avoidance
    private MoveWithPID(String name, VuforiaHandler vuforiaHandler, MovementHandler movementHandler, AbState nextState, EnumSet<SensorDetection> avoidedSides) {
        super(name);

        this.vuforiaHandler = vuforiaHandler;
        this.movementHandler = movementHandler;

        this.nextState = nextState; // will always be passed this

        this.avoidedSides = avoidedSides;
    }



    @Override
    public void init(AbState previousState) {
        linPID = new PID(); // reset pids here and init them in constructor?
        latPID = new PID();
        rotPID = new PID();



        // obj avoidance

        // check if there are any sides to be avoided
        if (!avoidedSides.isEmpty()) {
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
        }
    }



    @Override
    public AbState next() { // this might mess up
        EnumSet<SensorDetection> newDetections = detections.clone(); // edge cases: will not work if you avoid and then there's another obstacle (fixed by removing sides without detections)
        newDetections.removeAll(avoidedSides);

        if ((target[0] - robotX) < PRECISION && (target[1] - robotY) < PRECISION && (target[2] - robotR) < PRECISION){
            nextState.init(this);
            return nextState;
        }

        // if there is a new object and we're not near our target (if our target is something like a stack of rings)
        else if (!newDetections.isEmpty() &&
                !((target[0] - robotX) < PRECISION + SENSOR_PRECISION && (target[1] - robotY) < PRECISION + SENSOR_PRECISION && (target[2] - robotR) < PRECISION + SENSOR_PRECISION)) {
            avoidedSides.addAll(newDetections);
            return new MoveWithPID("Detour" + name, vuforiaHandler, movementHandler, nextState, avoidedSides);
        }

        else {
            return this;
        }

    }

    @Deprecated
    public float[] getTarget() {
        return target;
    }

    @Override
    public void run() { // TODO add avoidance
        robotX = vuforiaHandler.getRobotX();
        robotY = vuforiaHandler.getRobotY();
        robotR = vuforiaHandler.getRobotR();



        // obj avoidance:
        detections = movementHandler.getSensorDetections(SENSOR_PRECISION);

        currentPosition = new float[] {robotX, robotY, robotR};

        if (!avoidedSides.isEmpty() && bestDirection != null) { // if we're avoiding
            EnumSet<SensorDetection> openSides = EnumSet.complementOf(detections);
            actualTarget = bestDirection.getDetour(currentPosition); // find the target in the best direction
            avoidedSides.removeAll(openSides); // remove sides which are no longer being avoided
        }
        else {
            actualTarget = target;
        }



        double[] transformedError = movementHandler.errorTransformer(actualTarget[0] - robotX, actualTarget[1] - robotY, actualTarget[2] - robotR);
        float xError = (float) transformedError[0];
        float yError = (float) transformedError[1];
        float rError = actualTarget[2] - robotR;

        latPID.update(xError);
        linPID.update(yError);
        rotPID.update(rError);

        linIn = linPID.getInput();
        latIn = latPID.getInput();
        rotIn = rotPID.getInput();

        movementHandler.move(linIn, latIn, rotIn); // need to define exit conditions
    }
}
