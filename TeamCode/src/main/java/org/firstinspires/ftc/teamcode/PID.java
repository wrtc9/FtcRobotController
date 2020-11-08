package org.firstinspires.ftc.teamcode;
// there should be a pid for linear, lateral, and rotational movement
public class PID { // brug ftc has pidfs built in but theyre useless
    private float pastError = 0;

    private float kP = 0, kI = 0, kD = 0; // add default values for overloaded constructor

    private float P, I = 0, D; // actual values

    private final float CLAMP = 1;

    private float input;

    public PID(float kP, float kI, float kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }

    public PID() {}

    public void update(float error) { // P, I, and D should be between -1, 1
        P = kP * error;
        I += (kI * error >= CLAMP) ? CLAMP : kI * error; // clamping with the ternary; however, clamping might be a bit weird with how we're combining the elements
        D = (pastError == 0) ? 0 : kD * (error - pastError); // accounting for start when non differentiable

        input = P + I + D; // this might be better as an average but oh well

        pastError = error;
    }

    public float getInput() {
        return input;
    }
}
