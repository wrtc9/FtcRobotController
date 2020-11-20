package org.firstinspires.ftc.teamcode.states;

import org.firstinspires.ftc.teamcode.qol.Locations;
import org.firstinspires.ftc.teamcode.handlers.MovementHandler;
import org.firstinspires.ftc.teamcode.qol.Side;
import org.firstinspires.ftc.teamcode.handlers.VuforiaHandler;

public class MoveToWobbleZone extends AbState { // this can be done better
    private VuforiaHandler vuforiaHandler;
    private MovementHandler movementHandler;
    private Side side;
    private AbState nextState;

    private MoveWithPID moveToStack;
    private FindZone findZone;
    private RestState rest;

    public MoveToWobbleZone(String name, VuforiaHandler vuforiaHandler, MovementHandler movementHandler, Side side, AbState nextState) { // move to stack, detect stack height, go to zone
        super(name);
        this.vuforiaHandler = vuforiaHandler;
        this.movementHandler = movementHandler;
        this.side = side;
        this.nextState = nextState;
    }

    @Override
    public void init(AbState previousState) {
        moveToStack = new MoveWithPID("MoveToStack", vuforiaHandler, movementHandler, Locations.RING_STACK.getLocation(side), findZone);
        findZone = new FindZone("FindZone", vuforiaHandler, movementHandler, side, rest);
        rest = new RestState("Rest");
    }

    @Override
    public AbState next() {
        if (currentState == rest) {
            return nextState;
        }
        else {
            return this;
        }

        // this scares me, maybe change it to return currentState.next();? after thinking about it, this wont help; the state above will still have to go through this state
        // instead, think about end state maybe (when find zone ends), how do we translate this to next (add boolean isFinished();, this isn't satisfying either and won't work w/o another move child)
        // if (!endCondition) { return currentState; }, this better preserves the structure, but endCondition is still unknown
        // if (currentState != rest) { return currentState; }, better but still lacking
        // we should refactor rest to end, do checks if currentState is end (but this should just be done inherently somehow)
        // Q. How do we get end to indicate without ifs to stop running the state machine?
    }

    @Override
    public void run() {
        currentState.run();
        currentState = currentState.next(); // this is just covering it in another layer

        telemetryObjs = currentState.getTelemetry();
    }
}
