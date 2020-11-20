package org.firstinspires.ftc.teamcode.states;

import org.firstinspires.ftc.teamcode.handlers.MovementHandler;
import org.firstinspires.ftc.teamcode.qol.Side;
import org.firstinspires.ftc.teamcode.handlers.VuforiaHandler;
import org.firstinspires.ftc.teamcode.qol.WobbleSetting;

public class FindZone extends AbState { // this might be better if we use init with the next state (init is not guaranteed tho, so maybe this structure is better; look into it)
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
