package org.firstinspires.ftc.teamcode;

import java.util.ArrayList;

public class MoveToLocation extends AbState {

    private VuforiaHandler vuforiaHandler;
    private MovementHandler movementHandler;

    private AbState nextState;

    private Float robotX;
    private Float robotY;
    private Float robotR;

    private double[] target;

    private double deltaX;
    private double deltaY;
    private double deltaR;


    MoveToLocation(String name, VuforiaHandler vuforiaHandler, MovementHandler movementHandler, double[] target, AbState nextState) { // target should be 3 long [x,y,r]
        super(name);
        this.vuforiaHandler = vuforiaHandler;
        this.movementHandler = movementHandler;
        this.target = target;
        this.nextState = nextState;

        ArrayList<Float> robotXYR = vuforiaHandler.getRobotXYR();
        robotX = robotXYR.get(0);
        robotY = robotXYR.get(1);
        robotR = robotXYR.get(2);

        // just adding up the strafe, forward, and rotational stuff might not work; if they weighting is unique, calibration will prob have to be changed which will be a pain in the ass
        deltaX = target[0] - robotX;
        deltaY = target[1] - robotY;
        deltaR = target[2] - robotR; // movementHandler.thetaTo(deltaX, deltaY); changed because it prob won't work

        movementHandler.changeCurrentOpmode(MovementHandler.OpModeMode.AUTONOMOUS);
    }

    @Override
    public void run() {
        movementHandler.moveWithEncoders(deltaX, deltaY, deltaR);

        if (!movementHandler.isBusy()) {

            try {
                stateMachine.switchStates(nextState);
            } catch (NullPointerException e) { // check if this is the right exception, should throw if stateMachine is null
                e.printStackTrace();
                // brug if you're here you're dumb
            }

        }

    }
}
