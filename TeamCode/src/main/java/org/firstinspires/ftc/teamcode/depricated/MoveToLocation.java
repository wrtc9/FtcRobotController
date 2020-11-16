package org.firstinspires.ftc.teamcode.depricated;

import org.firstinspires.ftc.teamcode.MovementHandler;
import org.firstinspires.ftc.teamcode.VuforiaHandler;
import org.firstinspires.ftc.teamcode.AbState;

@Deprecated
public class MoveToLocation extends AbState { // this class shouldn't be constructed with all of this special stuff, it should be a super class

    private VuforiaHandler vuforiaHandler;
    private MovementHandler movementHandler;

    private AbState nextState;

    private float robotX;
    private float robotY;
    private float robotR;

    private float[] target;

    private float deltaX;
    private float deltaY;
    private float deltaR;


    MoveToLocation(String name, VuforiaHandler vuforiaHandler, MovementHandler movementHandler, float[] target, AbState nextState) {
        super(name);
        this.vuforiaHandler = vuforiaHandler;
        this.movementHandler = movementHandler;
        this.target = target;
        this.nextState = nextState;

        // just adding up the strafe, forward, and rotational stuff might not work; if they weighting is unique, calibration will prob have to be changed which will be a pain in the ass
         // movementHandler.thetaTo(deltaX, deltaY); changed because it prob won't work

        movementHandler.changeCurrentOpmode(MovementHandler.Mode.AUTONOMOUS);
    }

    @Override
    public void init(AbState previousState) {

    }

    @Override
    public AbState next() {
        if (!movementHandler.isBusy()){
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

        deltaX = target[0] - robotX;
        deltaY = target[1] - robotY;
        deltaR = target[2] - robotR;

        //movementHandler.moveWithEncoders(deltaX, deltaY, deltaR); // change to pid movement
    }
}
