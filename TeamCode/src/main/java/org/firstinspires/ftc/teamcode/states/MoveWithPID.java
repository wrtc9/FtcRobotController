package org.firstinspires.ftc.teamcode.states;

import org.firstinspires.ftc.teamcode.MovementHandler;
import org.firstinspires.ftc.teamcode.PID;
import org.firstinspires.ftc.teamcode.SensorDetection;
import org.firstinspires.ftc.teamcode.VuforiaHandler;
import org.firstinspires.ftc.teamcode.AbState;

import java.util.EnumSet;

// maybe change this to use inheritance
public class MoveWithPID extends AbState { // this state will forever move closer to the desired target; add tolerance and switch
    // TODO: Clean this up and check

    protected VuforiaHandler vuforiaHandler;
    protected MovementHandler movementHandler;

    protected AbState nextState;

    private PID linPID, latPID, rotPID;
    private float linIn, latIn, rotIn;

    private float[] target; // target must be in mm
    protected float robotX, robotY, robotR;

    protected final float precision = 1f, sensorPrecision = 5f * 24.3f, mmPerIn = 24.3f;

    MoveWithPID(String name, VuforiaHandler vuforiaHandler, MovementHandler movementHandler, float[] target, AbState nextState) {
        super(name);

        this.vuforiaHandler = vuforiaHandler;
        this.movementHandler = movementHandler;

        this.target = target; // should be in mm (x, y, r)
        target[0] *= mmPerIn;
        target[1] *= mmPerIn;

        this.nextState = nextState;
    }

    protected MoveWithPID(String name, AbState nextState) {
        super(name);
        this.nextState = nextState;
    }

    @Override
    public void init(AbState previousState) {
        linPID = new PID(); // reset pids here and init them in constructor?
        latPID = new PID();
        rotPID = new PID();
    }

    @Override
    public AbState next() { // this might mess up
        EnumSet<SensorDetection> detections = movementHandler.getSensorDetections(sensorPrecision);

        if (distanceCheck(precision)){
            nextState.init(this);
            return nextState;
        }

        else if (!detections.isEmpty() && !distanceCheck(precision + sensorPrecision)) { // we should try to phase the distance check out, only doing this so that we can get to the stack of rings
            return new MoveToAvoid("Detour" + name, this);
        }

        else {
            return this;
        }

    }

    @Deprecated // deprecated for now, but this is an alternative to making children control target with get target, instead controlling from the top
    public void setTarget(float[] target) {
        resetPIDS();
        this.target = target;
    }

    protected float[] getTarget() { // instead of having a MoveToAvoid class which extends this class, we could make a set target method which sets the target and resets the controllers and have the target be controlled from the outside
        return target;
    }

    public void resetPIDS() {
        linPID.reset();
        latPID.reset();
        rotPID.reset();
    }

    protected boolean distanceCheck(float precision) {
        return (target[0] - robotX) < precision && (target[1] - robotY) < precision && (target[2] - robotR) < precision;
    }

    @Override
    public void run() { // TODO add avoidance
        robotX = vuforiaHandler.getRobotX();
        robotY = vuforiaHandler.getRobotY();
        robotR = vuforiaHandler.getRobotR();

        target = getTarget();

        double[] transformedError = movementHandler.errorTransformer(target[0] - robotX, target[1] - robotY, target[2] - robotR);
        float xError = (float) transformedError[0];
        float yError = (float) transformedError[1];
        float rError = target[2] - robotR;

        latPID.update(xError);
        linPID.update(yError);
        rotPID.update(rError);

        linIn = linPID.getInput();
        latIn = latPID.getInput();
        rotIn = rotPID.getInput();

        movementHandler.move(linIn, latIn, rotIn, 100); // need to define exit conditions
    }
}
