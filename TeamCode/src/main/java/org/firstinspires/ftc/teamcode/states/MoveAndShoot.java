package org.firstinspires.ftc.teamcode.states;

import org.firstinspires.ftc.teamcode.qol.Locations;
import org.firstinspires.ftc.teamcode.handlers.MovementHandler;
import org.firstinspires.ftc.teamcode.qol.Side;
import org.firstinspires.ftc.teamcode.handlers.VuforiaHandler;
import org.firstinspires.ftc.teamcode.qol.Target;

public class MoveAndShoot extends AbState { // this makes me want to kms
    private IterativeMoveState moveState;
    private ShootState shootState;

    private final AbState nextState;

    MoveAndShoot(String name, VuforiaHandler vuforiaHandler, MovementHandler movementHandler, AbState nextState, Side side) {
        super(name);
        this.nextState = nextState;

        Target target = Locations.POWER_SHOT_LINE.getLocation();
        Target translation = new Target(7.5f, 0f, 0f);

        if (side == Side.RED) {
            target = target.getMirroredTarget();
            translation = translation.getMirroredTarget();
        }

        moveState = new IterativeMoveState("MoveAndShoot", target, translation, 3, shootState);
        shootState = new ShootState("ShootState", movementHandler, moveState);
        // with this structure we'll have to make multiple move and shoot states
        currentState = moveState;
    }

    @Override
    public void init(AbState previousState) {
    }

    @Override
    public AbState next() {
        if (currentState.getClass() == RestState.class){ // test this
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
