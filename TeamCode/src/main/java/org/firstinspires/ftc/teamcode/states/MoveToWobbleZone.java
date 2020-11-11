package org.firstinspires.ftc.teamcode.states;

import org.firstinspires.ftc.teamcode.AbState;
import org.firstinspires.ftc.teamcode.MovementHandler;
import org.firstinspires.ftc.teamcode.Side;
import org.firstinspires.ftc.teamcode.VuforiaHandler;

public class MoveToWobbleZone extends AbState { // this can be done better
    private VuforiaHandler vuforiaHandler;
    private MovementHandler movementHandler;
    private Side side;
    private AbState nextState;

    private MoveWithPID moveToStack;
    private FindZone findZone;

    public MoveToWobbleZone(String name, VuforiaHandler vuforiaHandler, MovementHandler movementHandler, Side side, AbState nextState) { // move to stack, detect stack height, go to zone
        super(name);
        this.vuforiaHandler = vuforiaHandler;
        this.movementHandler = movementHandler;
        this.side = side;
        this.nextState = nextState;
    }

    @Override
    public void init(AbState previousState) {
        moveToStack = new MoveWithPID("MoveToStack", vuforiaHandler, movementHandler, new float[] {-38f * side.getSign(), -42.75f, 180f}, findZone);
        findZone = new FindZone("FindZone", vuforiaHandler, movementHandler, side, nextState);
    }

    @Override
    public AbState next() {
        return this;
    }

    @Override
    public void run() {
        currentState.run();
        currentState = currentState.next();
    }
}
