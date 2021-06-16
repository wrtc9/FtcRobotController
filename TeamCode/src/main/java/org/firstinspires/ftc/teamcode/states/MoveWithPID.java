package org.firstinspires.ftc.teamcode.states;

import org.firstinspires.ftc.teamcode.handlers.MovementHandler;
import org.firstinspires.ftc.teamcode.handlers.PositionEstimator;
import org.firstinspires.ftc.teamcode.qol.PID;
import org.firstinspires.ftc.teamcode.qol.SensorDetection;
import org.firstinspires.ftc.teamcode.qol.Location;
import org.firstinspires.ftc.teamcode.qol.TelemetryInfo;
import org.firstinspires.ftc.teamcode.handlers.VuforiaHandler;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Locale;

/**
 * MoveWithPID uses PIDs to move directly to the specified target. It is also the super class to
 * {@link IterativeMoveState IterativeMoveState} and {@link MoveToAvoid MoveToAvoid}.
 *
 * @author Will (wrtc9)
 */
// maybe change this to use inheritance
public class MoveWithPID extends AbState { // this state will forever move closer to the desired target; add tolerance and switch
    // TODO: Clean this up and check

    protected VuforiaHandler vuforiaHandler;
    protected MovementHandler movementHandler;
    protected PositionEstimator positionEstimator;

    protected AbState nextState;

    //private PID linPID, latPID, rotPID;

    private Location target; // target must be in mm
    protected float robotX, robotY, robotR;

    private Location position = new Location(0, 0, 0);

    protected final float precision = 25.4f, sensorPrecision = 5f * 25.4f, rotationPrecision = 5f, robotWidth = 18f;
    private final double mmPerDeg = (robotWidth / Math.sqrt(2)) * 2 * Math.PI / 360; // r multiplied by 2pi (to get circumference) divided by 360 for degrees

    private final TelemetryInfo linInfo = new TelemetryInfo("LINEAR_INPUT:"),
            latInfo = new TelemetryInfo("LATERAL_INPUT:"),
            rotInfo = new TelemetryInfo("ROTATIONAL_INPUT:"),
            targetInfo = new TelemetryInfo("TARGET:"),
            estimatedPositionInfo = new TelemetryInfo("ESTIMATED_POSITION:"),
            speedInfo = new TelemetryInfo("SPEED"),
            errorInfo = new TelemetryInfo("ERROR"),
            unalteredErrorInfo = new TelemetryInfo("UNALTERED_ERROR");


    public MoveWithPID(String name, VuforiaHandler vuforiaHandler, MovementHandler movementHandler, Location target, AbState nextState) {
        super(name);

        this.vuforiaHandler = vuforiaHandler;
        this.movementHandler = movementHandler;

        this.target = target; // should be in mm (x, y, r)
        target.toMM();

        this.nextState = nextState;

        /*linPID = new PID();
        latPID = new PID();
        rotPID = new PID();*/

        telemetryObjs.add(linInfo);
        telemetryObjs.add(latInfo);
        telemetryObjs.add(rotInfo);
        telemetryObjs.add(targetInfo);
        telemetryObjs.add(estimatedPositionInfo);
        telemetryObjs.add(speedInfo);
        telemetryObjs.add(errorInfo);
        telemetryObjs.add(unalteredErrorInfo);

        positionEstimator = new PositionEstimator(movementHandler);
    }

    protected MoveWithPID(String name, AbState nextState) { // used with MoveToAvoid
        super(name);
        this.nextState = nextState;
    }

    @Override
    public void init(AbState previousState) {
    }

    @Override
    public AbState next() { // this might mess up
        EnumSet<SensorDetection> detections = movementHandler.getSensorDetections(sensorPrecision);

        if (distanceCheck(precision, rotationPrecision)){
            movementHandler.move(1, 1, 1, 0);
            return nextState;
        }

        else if (!detections.isEmpty() && !distanceCheck(precision + sensorPrecision, rotationPrecision)) { // we should try to phase the distance check out, only doing this so that we can get to the stack of rings
            return new MoveToAvoid("Detour" + name, this, target);
        }

        else {
            return this;
        }

    }

    /**
     * getTarget is designed to be overridden by a sub-class to dynamically change the target
     * position.
     * @return Target position
     */
    protected Location getTarget() { // instead of having a MoveToAvoid class which extends this class, we could make a set target method which sets the target and resets the controllers and have the target be controlled from the outside
        return target;
    }

    public void resetPIDS() {
        /*linPID.reset();
        latPID.reset();
        rotPID.reset();*/
    }

    protected boolean distanceCheck(float precision, float rotationPrecision) { // this could probably be made a little better
        return (target.getX() - position.getX()) < precision && (target.getY() - position.getY()) < precision && (target.getR() - position.getR()) < rotationPrecision;
    }

    private float clip(float compare, float max, float min) {
        if (Math.abs(compare) > max) {
            return Math.signum(compare) * max;
        }
        else if (Math.abs(compare) < min) {
            return Math.signum(compare) * min;
        }
        else {
            return compare;
        }
    }

    private float clip(float num) {
        return clip(num, 1, 0);
    }

    @Override
    public void run() { // TODO add avoidance
        if (vuforiaHandler.isTargetVisible()) {
            robotX = vuforiaHandler.getRobotX();
            robotY = vuforiaHandler.getRobotY();
            robotR = vuforiaHandler.getRobotR();

            position.setX(robotX);
            position.setY(robotY);
            position.setR(robotR);

            positionEstimator.reset();
        }
        else {
            position = positionEstimator.update(position);
            estimatedPositionInfo.setContent(String.format(Locale.ENGLISH, "{X, Y, R} = %.1f, %.1f, %.1f",
                    position.getX()/25.4, position.getY()/25.4, position.getR()));
        }

        target = getTarget(); //dumb

        unalteredErrorInfo.setContent(String.format(Locale.ENGLISH, "X: %f, Y: %f, R: %f", target.getX() - position.getX(), target.getY() - position.getY(), position.getR()));

        double[] transformedError = movementHandler.errorTransformer(target.getX() - position.getX(), target.getY() - position.getY(), position.getR()); // make this better suited to the Target type
        float xError = (float) transformedError[0];
        float yError = (float) transformedError[1];
        float rError = target.getR() - position.getR(); // multiplied by mmPerDeg for proper weighting
        /*xError = (Math.abs(xError) >= precision) ? xError : 0f;
        yError = (Math.abs(yError) >= precision) ? yError : 0f;
        rError = (Math.abs(rError) >= rotationPrecision) ? rError : 0f;*/

        errorInfo.setContent(String.format(Locale.ENGLISH, "X: %f, Y: %f, R: %f", xError/25.4, yError/25.4, rError));

        /*latPID.update(xError);
        linPID.update(yError);
        rotPID.update(rError);

        float linIn = linPID.getInput()/(25.4f*144f);
        float latIn = latPID.getInput()/(25.4f*144f);
        float rotIn = rotPID.getInput()/360;*/

        float latIn = xError / (25.4f * 144f); // TODO change to dividing by speeds
        float linIn = yError / (25.4f * 144f);
        float rotIn = rError / 360;
        clip(linIn); // just in case
        clip(latIn);
        clip(rotIn);

        linInfo.setContent(String.valueOf(linIn));
        latInfo.setContent(String.valueOf(latIn));
        rotInfo.setContent(String.valueOf(rotIn));

        float speed = clip(Collections.max(Arrays.asList(linIn, latIn, rotIn)), 0.7f, 0.3f);

        speedInfo.setContent(String.valueOf(speed));

        targetInfo.setContent(String.format(Locale.ENGLISH, "X: %f, Y: %f, R: %f", target.getX()/25.4, target.getY()/25.4, target.getR()));

        movementHandler.move(linIn, latIn, rotIn, speed); // need to define exit conditions
    }
}
