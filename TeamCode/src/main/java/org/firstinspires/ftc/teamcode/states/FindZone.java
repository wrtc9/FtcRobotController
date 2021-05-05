package org.firstinspires.ftc.teamcode.states;

import org.firstinspires.ftc.teamcode.handlers.MovementHandler;
import org.firstinspires.ftc.teamcode.qol.Locations;
import org.firstinspires.ftc.teamcode.qol.Side;
import org.firstinspires.ftc.teamcode.handlers.VuforiaHandler;
import org.firstinspires.ftc.teamcode.qol.Location;

/**
 * FindZone builds the move state that goes to the wobble zone and returns it through next
 * @author Will (wrtc9)
 */
public class FindZone extends AbState { // this might be better if we use init with the next state (init is not guaranteed tho, so maybe this structure is better; look into it)
    private final VuforiaHandler vuforiaHandler;
    private final MovementHandler movementHandler;
    private final Side side;
    private final AbState nextState;

    private MoveWithPID moveToZone; // built in state

    private boolean found;
    private Location correctTarget;

    private EndState endState = new EndState("end of finding");

    FindZone(String name, VuforiaHandler vuforiaHandler, MovementHandler movementHandler, Side side, AbState nextState) {
        super(name);
        this.vuforiaHandler = vuforiaHandler;
        this.movementHandler = movementHandler;
        this.side = side;
        this.nextState = nextState;
    }

    @Override
    public void init(AbState previousState) {
        MoveWithPID moveLeft, moveRight;
        float x = vuforiaHandler.getRobotX(), y = vuforiaHandler.getRobotY(), r = vuforiaHandler.getRobotR();
        moveRight = new MoveWithPID("move to the left", vuforiaHandler, movementHandler, new Location(x+10,y,r), endState);
        moveLeft = new MoveWithPID("move to the left", vuforiaHandler, movementHandler, new Location(x-10,y,r), moveRight);
        currentState = moveLeft;
    }

    @Override
    public AbState next() {
        if (found) {
            return new MoveWithPID("moveToZone", vuforiaHandler, movementHandler, correctTarget, nextState);
        }
        else {
            return this;
        }
    } // immediately returns the state to move to the correct zone

    @Override
    public void run() {
        /*WobbleSetting wobbleSetting = movementHandler.findWobble();
        Location correctTarget = wobbleSetting.getTarget(); // should this return a formatted sign for side

        if (side == Side.RED) {
            correctTarget = correctTarget.getMirroredTarget();
        }*/

        // let's just trust that we're at the stack


        runMachine();
        found = true;
        double sensorValue = movementHandler.getWobbleSensor(); // change in future to default distance to ground
        if (sensorValue > 1) {
            correctTarget = Locations.A.getLocation();
        }
        else if (sensorValue > 2) {
            correctTarget = Locations.B.getLocation();
        }
        else if (currentState.equals(endState)) { // find a better way of doing this
            correctTarget = Locations.C.getLocation();
        }
        else {
            found = false;
        }
    }
}
