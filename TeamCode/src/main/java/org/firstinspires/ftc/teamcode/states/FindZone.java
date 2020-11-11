package org.firstinspires.ftc.teamcode.states;

import org.firstinspires.ftc.teamcode.AbState;
import org.firstinspires.ftc.teamcode.MovementHandler;
import org.firstinspires.ftc.teamcode.Side;
import org.firstinspires.ftc.teamcode.VuforiaHandler;
import org.firstinspires.ftc.teamcode.WobbleSetting;

public class FindZone extends AbState { // returns a state for going to the next zone
    private VuforiaHandler vuforiaHandler;
    private MovementHandler movementHandler;
    private Side side;
    private AbState nextState;

    private MoveWithPID moveToZone; // built in state

    private WobbleSetting wobbleSetting;

    public FindZone(String name, VuforiaHandler vuforiaHandler, MovementHandler movementHandler, Side side, AbState nextState) {
        super(name);
        this.vuforiaHandler = vuforiaHandler;
        this.movementHandler = movementHandler;
        this.side = side;
        this.nextState = nextState;
    }

    @Override
    public void init(AbState previousState) {

    }

    @Override
    public AbState next() {
        return moveToZone;
    }

    @Override
    public void run() {
        wobbleSetting = movementHandler.findWobble();
        float[] correctTarget = wobbleSetting.getTarget(side);

        moveToZone = new MoveWithPID("moveToZone", vuforiaHandler, movementHandler, correctTarget, nextState);
    }
}
