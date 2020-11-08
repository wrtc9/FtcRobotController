package org.firstinspires.ftc.teamcode.states;

import org.firstinspires.ftc.teamcode.AbState;
import org.firstinspires.ftc.teamcode.MovementHandler;
import org.firstinspires.ftc.teamcode.VuforiaHandler;

public class MoveAndShoot extends AbState { // this makes me want to kms
    private AbState moveState, shootState;

    private VuforiaHandler vuforiaHandler;
    private MovementHandler movementHandler;

    private AbState nextState;

    private final float PRECISION = 1;

    MoveAndShoot(String name, VuforiaHandler vuforiaHandler, MovementHandler movementHandler, AbState nextState) {
        super(name);
        this.vuforiaHandler = vuforiaHandler;
        this.movementHandler = movementHandler;

        moveState = new IterativeMoveState("MoveAndShoot", vuforiaHandler, movementHandler, new float[]{-13.625f, 8.25f, -90f}, new float[]{0f, 7.5f, 0f}, shootState);
        shootState = new ShootState("ShootState", vuforiaHandler, movementHandler, moveState);
        // with this structure we'll have to make multiple move and shoot states
        currentState = moveState;
    }

    @Override
    public void init() {

    }

    @Override
    public AbState next() {
        if (Math.abs(23.25 - vuforiaHandler.getRobotX()) < PRECISION){
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
        currentState.next();
    }
}
