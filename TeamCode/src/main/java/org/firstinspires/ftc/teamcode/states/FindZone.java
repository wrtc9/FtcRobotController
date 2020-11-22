package org.firstinspires.ftc.teamcode.states;

import org.firstinspires.ftc.teamcode.handlers.MovementHandler;
import org.firstinspires.ftc.teamcode.qol.Side;
import org.firstinspires.ftc.teamcode.handlers.VuforiaHandler;
import org.firstinspires.ftc.teamcode.qol.Target;
import org.firstinspires.ftc.teamcode.qol.WobbleSetting;

public class FindZone extends AbState { // this might be better if we use init with the next state (init is not guaranteed tho, so maybe this structure is better; look into it)
    private final VuforiaHandler vuforiaHandler;
    private final MovementHandler movementHandler;
    private final Side side;
    private final AbState nextState;

    private MoveWithPID moveToZone; // built in state

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
    } // immediately returns the state to move to the correct zone

    @Override
    public void run() {
        WobbleSetting wobbleSetting = movementHandler.findWobble();
        Target correctTarget = wobbleSetting.getTarget(); // should this return a formatted sign for side

        if (side == Side.RED) {
            correctTarget = correctTarget.getMirroredTarget();
        }

        moveToZone = new MoveWithPID("moveToZone", vuforiaHandler, movementHandler, correctTarget, nextState);
    }
}
