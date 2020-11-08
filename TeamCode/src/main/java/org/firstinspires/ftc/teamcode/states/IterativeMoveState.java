package org.firstinspires.ftc.teamcode.states;

import org.firstinspires.ftc.teamcode.AbState;
import org.firstinspires.ftc.teamcode.MovementHandler;
import org.firstinspires.ftc.teamcode.VuforiaHandler;

public class IterativeMoveState extends MoveWithPID {
    private float[] translation;
    IterativeMoveState(String name, VuforiaHandler vuforiaHandler, MovementHandler movementHandler, float[] target, float[] translation, AbState nextState) {
        super(name, vuforiaHandler, movementHandler, target, nextState);
        this.translation = translation;
    }

    public AbState condition(){
        if ((target[0] - robotX) < PRECISION && (target[1] - robotY) < PRECISION && (target[2] - robotR) < PRECISION){
            for (int i = 0; i < target.length; i++) { // stupid stupid stupid stupid stupid
                target[i] += translation[i];
            }
            return nextState;
        }
        else {
            return this;
        }
    }
}
