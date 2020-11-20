package org.firstinspires.ftc.teamcode.states;

import org.firstinspires.ftc.teamcode.qol.SensorDetection;

import java.util.EnumSet;

public class IterativeMoveState extends MoveWithPID { // need to change this to be in line with MoveAndAvoid
    private final float[] translation;
    private float[] target;

    private int repetitions = 0;
    private int maxRepetitions;

    protected IterativeMoveState(String name, float[] start, float[] translation, int maxRepetitions, AbState nextState) {
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
        else if ((target[0] - robotX) < precision && (target[1] - robotY) < precision && (target[2] - robotR) < precision){
            nextState.init(this);
            repetitions++;

            for (int i = 0; i < 3; i++) {
                target[i] += translation[i];
            }

            return nextState;
        }

        else if (!detections.isEmpty()) {
            return new MoveToAvoid("Detour" + name, this);
        }

        else {
            return this;
        }

    }

    protected float[] getTarget() {
        return target;
    }

    public int getRepetitions() {
        return repetitions;
    }
}
