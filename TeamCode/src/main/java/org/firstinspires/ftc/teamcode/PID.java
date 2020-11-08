package org.firstinspires.ftc.teamcode;
// there should be a pid for linear, lateral, and rotational movement
public class PID { // brug ftc has pidfs built in but theyre useless
    private float pastError = 0;

    private float kP = 1f, kI = 0.1f, kD = 0.01f; // add default values for overloaded constructor

    private float P, I = 0, D; // actual values

    private final float CLAMP = 144 * 24.3f; // currently set to max error, though I'm not sure if this is the best value

    private float input;

    public PID(float kP, float kI, float kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }

    public PID() {}

    public void update(float error) { // P, I, and D should be between -1, 1
        P = kP * error;
        I += (Math.abs(kI * error) >= CLAMP) ? Math.signum(error) * CLAMP : kI * error; // not sure if clamp is necessary since MovementHandler sorta clamps for us
        D = (pastError == 0) ? 0 : kD * (error - pastError); // accounting for start when non differentiable

        input = P + I + D; // this might be better as an average but oh well

        pastError = error;
    }

    public float getInput() {
        return input;
    }
}
