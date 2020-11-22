package org.firstinspires.ftc.teamcode.states;

import org.firstinspires.ftc.teamcode.qol.Locations;
import org.firstinspires.ftc.teamcode.handlers.MovementHandler;
import org.firstinspires.ftc.teamcode.qol.Side;
import org.firstinspires.ftc.teamcode.handlers.VuforiaHandler;
import org.firstinspires.ftc.teamcode.qol.Target;

public class MoveToWobbleZone extends AbState { // this can be done better
    private final AbState nextState;

    private MoveWithPID moveToStack;
    private FindZone findZone;
    private RestState rest;

    public MoveToWobbleZone(String name, VuforiaHandler vuforiaHandler, MovementHandler movementHandler, Side side, AbState nextState) { // move to stack, detect stack height, go to zone
        super(name);
        this.nextState = nextState;

        Target target = Locations.RING_STACK.getLocation();

        if (side == Side.RED) {
            target = target.getMirroredTarget();
        }

        moveToStack = new MoveWithPID("MoveToStack", vuforiaHandler, movementHandler, target, findZone);
        findZone = new FindZone("FindZone", vuforiaHandler, movementHandler, side, rest);
        rest = new RestState("Rest");
    }

    @Override
    public void init(AbState previousState) {
    }

    @Override
    public AbState next() {
        if (currentState == rest) {
            return nextState;
        }
        else {
            return this;
        }
    }

    @Override
    public void run() {
        currentState.run();
        currentState = currentState.next(); // this is just covering it in another layer

        telemetryObjs = currentState.getTelemetry();
    }
}
