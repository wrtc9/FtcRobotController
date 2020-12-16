package org.firstinspires.ftc.teamcode.states;

import org.firstinspires.ftc.teamcode.handlers.MovementHandler;
import org.firstinspires.ftc.teamcode.qol.PID;
import org.firstinspires.ftc.teamcode.qol.SensorDetection;
import org.firstinspires.ftc.teamcode.qol.Target;
import org.firstinspires.ftc.teamcode.qol.TelemetryInfo;
import org.firstinspires.ftc.teamcode.handlers.VuforiaHandler;

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

    protected AbState nextState;

    private PID linPID, latPID, rotPID;

    private Target target; // target must be in mm
    protected float robotX, robotY, robotR;

    protected final float precision = 1f, sensorPrecision = 5f * 25.4f, robotWidth = 18f;
    private final double mmPerDeg = (robotWidth / Math.sqrt(2)) * 2 * Math.PI / 360; // r multiplied by 2pi (to get circumference) divided by 360 for degrees

    private final TelemetryInfo linInfo = new TelemetryInfo("LINEAR_INPUT:"),
                                latInfo = new TelemetryInfo("LATERAL_INPUT:"),
                                rotInfo = new TelemetryInfo("ROTATIONAL_INPUT:"),
                                targetInfo = new TelemetryInfo("TARGET:");

    MoveWithPID(String name, VuforiaHandler vuforiaHandler, MovementHandler movementHandler, Target target, AbState nextState) {
        super(name);

        this.vuforiaHandler = vuforiaHandler;
        this.movementHandler = movementHandler;

        this.target = target; // should be in mm (x, y, r)
        target.toMM();

        this.nextState = nextState;

        linPID = new PID();
        latPID = new PID();
        rotPID = new PID();

        telemetryObjs.add(linInfo);
        telemetryObjs.add(latInfo);
        telemetryObjs.add(rotInfo);
        telemetryObjs.add(targetInfo);
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

        if (distanceCheck(precision)){
            return nextState;
        }

        else if (!detections.isEmpty() && !distanceCheck(precision + sensorPrecision)) { // we should try to phase the distance check out, only doing this so that we can get to the stack of rings
            return new MoveToAvoid("Detour" + name, this, target);
        }

        else {
            return this;
        }

    }

    @Deprecated // deprecated for now, but this is an alternative to making children control target with get target, instead controlling from the top
    public void setTarget(Target target) {
        resetPIDS();
        this.target = target;
    }

    /**
     * getTarget is designed to be overridden by a sub-class to dynamically change the target
     * position.
     * @return Target position
     */
    protected Target getTarget() { // instead of having a MoveToAvoid class which extends this class, we could make a set target method which sets the target and resets the controllers and have the target be controlled from the outside
        return target;
    }

    public void resetPIDS() {
        linPID.reset();
        latPID.reset();
        rotPID.reset();
    }

    protected boolean distanceCheck(float precision) { // this could probably be made a little better
        return (target.getX() - robotX) < precision && (target.getY() - robotY) < precision && (target.getR() - robotR) < precision;
    }

    @Override
    public void run() { // TODO add avoidance
        robotX = vuforiaHandler.getRobotX();
        robotY = vuforiaHandler.getRobotY();
        robotR = vuforiaHandler.getRobotR();

        target = getTarget();

        double[] transformedError = movementHandler.errorTransformer(target.getX() - robotX, target.getY() - robotY, robotR); // make this better suited to the Target type
        float xError = (float) transformedError[0];
        float yError = (float) transformedError[1];
        float rError = (target.getY() - robotR) * (float) mmPerDeg; // multiplied by mmPerDeg for proper weighting

        latPID.update(xError);
        linPID.update(yError);
        rotPID.update(rError);

        float linIn = linPID.getInput();
        float latIn = latPID.getInput();
        float rotIn = rotPID.getInput();

        linInfo.setContent(String.valueOf(linIn));
        latInfo.setContent(String.valueOf(latIn));
        rotInfo.setContent(String.valueOf(rotIn));

        targetInfo.setContent(String.format(Locale.ENGLISH,"X: %f, Y: %f, R: %f", target.getX(), target.getY(), target.getR()));

        movementHandler.move(linIn, latIn, rotIn, 100); // need to define exit conditions
    }
}
