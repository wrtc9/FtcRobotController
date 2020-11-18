package org.firstinspires.ftc.teamcode.states;

import org.firstinspires.ftc.teamcode.AbState;
import org.firstinspires.ftc.teamcode.MovementHandler;
import org.firstinspires.ftc.teamcode.Side;
import org.firstinspires.ftc.teamcode.VuforiaHandler;

import java.util.ArrayList;

public class DefaultAuto extends AbState {
    private VuforiaHandler vuforiaHandler;
    private MovementHandler movementHandler;
    private Side side;

    private AbState moveAndShoot, moveToWobbleZone, rest;

    public DefaultAuto(String name, VuforiaHandler vuforiaHandler, MovementHandler movementHandler, Side side) { // idea: put this in a package with the other states
        super(name);
        this.vuforiaHandler = vuforiaHandler;
        this.movementHandler = movementHandler;
        this.side = side;
    }

    @Override
    public void init(AbState previousState) {
        moveAndShoot = new MoveAndShoot("MoveAndShoot", vuforiaHandler, movementHandler, moveToWobbleZone, side);
        moveToWobbleZone = new MoveToWobbleZone("MoveToWobbleZone", vuforiaHandler, movementHandler, side, rest);
        rest = new RestState("Rest");
        currentState = moveAndShoot;
    }

    @Override
    public AbState next() {
        return this;
    }
    // idea: if (currentState != end) { return this; }; this will work better if we want to nest further and should be the preferred nested state next(); this causes issues for this state
    // something special should happen with end and the parent should know (eg: special telemetry for end state; should be the same structure as other nested states)
    // then again, currentState != end should only be used when the end of the state machine is when the final state terminates
    // explore this idea of state machines terminating

    @Override
    public void run() {
        currentState.run();
        currentState = currentState.next();

        telemetryObjs = currentState.getTelemetry();
    }
}
