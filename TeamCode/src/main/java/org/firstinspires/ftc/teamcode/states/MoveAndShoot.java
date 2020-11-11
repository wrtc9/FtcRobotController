package org.firstinspires.ftc.teamcode.states;

import org.firstinspires.ftc.teamcode.AbState;
import org.firstinspires.ftc.teamcode.MovementHandler;
import org.firstinspires.ftc.teamcode.Side;
import org.firstinspires.ftc.teamcode.VuforiaHandler;

public class MoveAndShoot extends AbState { // this makes me want to kms
    private IterativeMoveState moveState;
    private ShootState shootState;

    private VuforiaHandler vuforiaHandler;
    private MovementHandler movementHandler;
    private Side side;

    private AbState nextState;

    private final float precision = 1f;
    private final float mmPerIn = 24.3f;

    MoveAndShoot(String name, VuforiaHandler vuforiaHandler, MovementHandler movementHandler, AbState nextState, Side side) {
        super(name);
        this.vuforiaHandler = vuforiaHandler;
        this.movementHandler = movementHandler;
        this.side = side;
        this.nextState = nextState;
    }

    @Override
    public void init(AbState previousState) {
        moveState = new IterativeMoveState("MoveAndShoot", vuforiaHandler, movementHandler,
                new float[]{-13.625f * side.getSign(), 8.25f, -90f}, // x-values are mirrored
                new float[]{7.5f * side.getSign(), 0f, 0f}, shootState);

        shootState = new ShootState("ShootState", vuforiaHandler, movementHandler, moveState);
        // with this structure we'll have to make multiple move and shoot states
        currentState = moveState;
    }

    @Override
    public AbState next() {
        if (moveState.getRepetitions() >= 3){ // test this
            nextState.init(this);
            return nextState;
        }
        else {
            return this;
        }
    }

    @Override
    public void run() {
        currentState.run();
        currentState = currentState.next();
    }
}
