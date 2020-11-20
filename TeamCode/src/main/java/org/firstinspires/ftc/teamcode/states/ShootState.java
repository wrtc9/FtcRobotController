package org.firstinspires.ftc.teamcode.states;

import org.firstinspires.ftc.teamcode.handlers.MovementHandler;
import org.firstinspires.ftc.teamcode.handlers.VuforiaHandler;

public class ShootState extends AbState {
    private VuforiaHandler vuforiaHandler;
    private MovementHandler movementHandler;
    private AbState nextState;

    ShootState(String name, VuforiaHandler vuforiaHandler, MovementHandler movementHandler, AbState nextState) {
        super(name);
        this.vuforiaHandler = vuforiaHandler;
        this.movementHandler = movementHandler;
        this.nextState = nextState;
    }

    @Override
    public void init(AbState previousState) {
        // init motors and such here
    }

    @Override
    public AbState next() {
        nextState.init(this);
        return nextState;
    }

    @Override
    public void run() {
        movementHandler.shoot(); // shoot needs to be defined
    }
}
