package org.firstinspires.ftc.teamcode.states;

import org.firstinspires.ftc.teamcode.MovementHandler;
import org.firstinspires.ftc.teamcode.PID;
import org.firstinspires.ftc.teamcode.VuforiaHandler;
import org.firstinspires.ftc.teamcode.AbState;

// maybe change this to use inheritance
public class MoveWithPID extends AbState { // this state will forever move closer to the desired target; add tolerance and switch
    private VuforiaHandler vuforiaHandler;
    private MovementHandler movementHandler;

    protected float[] target; // target must be in mm

    protected AbState nextState;

    protected float robotX, robotY, robotR;

    private PID linPID, latPID, rotPID;
    private float linIn, latIn, rotIn;

    protected final float PRECISION = 1;

    private final float mmPerIn = 24.3f;

    MoveWithPID(String name, VuforiaHandler vuforiaHandler, MovementHandler movementHandler, float[] target, AbState nextState) { // think about whether we should put stuff in constructor or init
        super(name);

        this.vuforiaHandler = vuforiaHandler;
        this.movementHandler = movementHandler;

        this.target = target; // should be in mm (x, y, r)
        target[0] *= mmPerIn;
        target[1] *= mmPerIn;

        this.nextState = nextState;
    }

    @Override
    public void init(AbState previousState) {
        linPID = new PID(); // maybe this should be done in init to reduce memory usage
        latPID = new PID();
        rotPID = new PID();
    }

    @Override
    public AbState next() {
        if ((target[0] - robotX) < PRECISION && (target[1] - robotY) < PRECISION && (target[2] - robotR) < PRECISION){
            nextState.init(this);
            return nextState;
        }
        else {
            return this;
        }
    }

    @Override
    public void run() {
        robotX = vuforiaHandler.getRobotX();
        robotY = vuforiaHandler.getRobotY();
        robotR = vuforiaHandler.getRobotR();

        latPID.update(target[0] - robotX);
        linPID.update(target[1] - robotY);
        rotPID.update(target[1] - robotR);

        linIn = linPID.getInput();
        latIn = latPID.getInput();
        rotIn = rotPID.getInput();

        movementHandler.move(linIn, latIn, rotIn); // need to define exit conditions
    }
}
