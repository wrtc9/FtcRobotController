package org.firstinspires.ftc.teamcode.states;

import org.firstinspires.ftc.teamcode.AbState;
import org.firstinspires.ftc.teamcode.MovementHandler;
import org.firstinspires.ftc.teamcode.Side;
import org.firstinspires.ftc.teamcode.VuforiaHandler;

public class MoveAndShoot extends AbState { // this makes me want to kms
    private AbState moveState, shootState;

    private VuforiaHandler vuforiaHandler;
    private MovementHandler movementHandler;

    private AbState nextState;

    private final float precision = 1f;
    private final float mmPerIn = 24.3f;

    MoveAndShoot(String name, VuforiaHandler vuforiaHandler, MovementHandler movementHandler, AbState nextState, Side side) {
        super(name);
        this.vuforiaHandler = vuforiaHandler;
        this.movementHandler = movementHandler;

        moveState = new IterativeMoveState("MoveAndShoot", vuforiaHandler, movementHandler,
                new float[]{-13.625f * side.getSign() * mmPerIn, 8.25f * mmPerIn, -90f},
                new float[]{7.5f * side.getSign() * mmPerIn, 0f * mmPerIn, 0f}, shootState);

        shootState = new ShootState("ShootState", vuforiaHandler, movementHandler, moveState);
        // with this structure we'll have to make multiple move and shoot states
        currentState = moveState;
    }

    @Override
    public void init() {

    }

    @Override
    public AbState next() {
        if (Math.abs(23.25 - vuforiaHandler.getRobotX()) < precision){ // change this
            nextState.init();
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
