package org.firstinspires.ftc.teamcode.states;

import org.firstinspires.ftc.teamcode.handlers.MovementHandler;
import org.firstinspires.ftc.teamcode.handlers.VuforiaHandler;

/**
 * ShootState runs {@link MovementHandler movementHandler's} shoot
 *
 * @author Will (wrtc9)
 */
public class ShootState extends AbState { // robot does not have shooting altitude control, so change the default opmode to move in the position which hits the powershots
    private final MovementHandler movementHandler;
    private final AbState nextState;

    ShootState(String name, MovementHandler movementHandler, AbState nextState) {
        super(name);
        this.movementHandler = movementHandler;
        this.nextState = nextState;
    }

    @Override
    public void init(AbState previousState) {
    }

    @Override
    public AbState next() {
        return nextState;
    }

    @Override
    public void run() {
        movementHandler.shoot(1, 1000); // shoot needs to be defined
    }
}
