package org.firstinspires.ftc.teamcode.states;

import org.firstinspires.ftc.teamcode.AbState;
import org.firstinspires.ftc.teamcode.MovementHandler;
import org.firstinspires.ftc.teamcode.Side;
import org.firstinspires.ftc.teamcode.VuforiaHandler;

public class DefaultAuto extends AbState {
    private VuforiaHandler vuforiaHandler;
    private MovementHandler movementHandler;
    private AbState moveAndShoot, moveToOrigin, rest;

    public DefaultAuto(String name, VuforiaHandler vuforiaHandler, MovementHandler movementHandler, Side side) { // idea: put this in a package with the other states
        super(name);
        this.vuforiaHandler = vuforiaHandler;
        this.movementHandler = movementHandler;

        moveAndShoot = new MoveAndShoot("MoveAndShoot", vuforiaHandler, movementHandler, moveToOrigin, side);
        moveToOrigin = new MoveWithPID("MoveToOrigin", vuforiaHandler, movementHandler, new float[]{0f, 0f, 0f}, rest);
        rest = new RestState("Rest");
        currentState = moveAndShoot;
    }

    @Override
    public void init() {

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
