package org.firstinspires.ftc.teamcode.states;

import org.firstinspires.ftc.teamcode.qol.Locations;
import org.firstinspires.ftc.teamcode.handlers.MovementHandler;
import org.firstinspires.ftc.teamcode.qol.Side;
import org.firstinspires.ftc.teamcode.handlers.VuforiaHandler;

public class MoveAndShoot extends AbState { // this makes me want to kms
    private IterativeMoveState moveState;
    private ShootState shootState;

    private VuforiaHandler vuforiaHandler;
    private MovementHandler movementHandler;
    private Side side;

    private AbState nextState;

    private final float precision = 1f;
    private final float mmPerIn = 25.4f;

    MoveAndShoot(String name, VuforiaHandler vuforiaHandler, MovementHandler movementHandler, AbState nextState, Side side) {
        super(name);
        this.vuforiaHandler = vuforiaHandler;
        this.movementHandler = movementHandler;
        this.side = side;
        this.nextState = nextState;
    }

    @Override
    public void init(AbState previousState) {
        moveState = new IterativeMoveState("MoveAndShoot", Locations.POWER_SHOT_LINE.getLocation(side), new float[]{7.5f * side.getSign(), 0f, 0f}, 3, shootState);
        shootState = new ShootState("ShootState", vuforiaHandler, movementHandler, moveState);
        // with this structure we'll have to make multiple move and shoot states
        currentState = moveState;
    }

    @Override
    public AbState next() {
        if (moveState.getRepetitions() >= 3){ // test this
            nextState.init(this); // would it be better for this to be inside iterative move state? (definitely do this, makes a lot of sense for the parent not to be inquiring about it)
            return nextState; // that would bring it in line with the idea of terminating machines
        }
        else {
            return this;
        }
    }

    @Override
    public void run() {
        currentState.run();
        currentState = currentState.next();

        telemetryObjs = currentState.getTelemetry();
    }
}
