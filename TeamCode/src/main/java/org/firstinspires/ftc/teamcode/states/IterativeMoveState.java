package org.firstinspires.ftc.teamcode.states;

import org.firstinspires.ftc.teamcode.qol.SensorDetection;
import org.firstinspires.ftc.teamcode.qol.Target;

import java.util.EnumSet;

public class IterativeMoveState extends MoveWithPID { // need to change this to be in line with MoveAndAvoid
    private final Target translation;
    private final Target target;

    private int repetitions = 0;
    private final int maxRepetitions;

    protected IterativeMoveState(String name, Target start, Target translation, int maxRepetitions, AbState nextState) {
        super(name, nextState);

        this.target = start;
        this.translation = translation;
        this.maxRepetitions = maxRepetitions;
    }


    public AbState next() { // this might mess up
        EnumSet<SensorDetection> detections = movementHandler.getSensorDetections(sensorPrecision);

        if (repetitions > maxRepetitions) {
            return new RestState("Rest");
        }
        else if ((target.getX() - robotX) < precision && (target.getY() - robotY) < precision && (target.getR() - robotR) < precision){
            nextState.init(this);
            repetitions++;

            target.setX(target.getX() + translation.getX());
            target.setY(target.getY() + translation.getY());
            target.setR(target.getR() + translation.getR());

            return nextState;
        }

        else if (!detections.isEmpty()) {
            return new MoveToAvoid("Detour" + name, this, target);
        }

        else {
            return this;
        }

    }

    protected Target getTarget() {
        return target;
    }

    public int getRepetitions() {
        return repetitions;
    }
}
