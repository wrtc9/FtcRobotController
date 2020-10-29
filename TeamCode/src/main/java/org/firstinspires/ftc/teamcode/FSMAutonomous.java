package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.Locale;

/* TODO
    - Add states
 */
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "FSMAutonomous", group = "Autonomous")
public class FSMAutonomous extends OpMode {
    public VuforiaHandler vuforiaHandler;
    public MovementHandler movementHandler;

    private StateMachine stateMachine;
    private MoveToLocation moveToLineExample;
    private RestState restState;

    private static double MM_PER_INCH = 25.4;

    private DcMotor leftFront;
    private DcMotor rightFront;
    private DcMotor leftRear;
    private DcMotor rightRear;

    /* public Float robotX;
    public Float robotY;
    public Float robotR; */

    @Override
    public void init() {
        // initialize motors
        vuforiaHandler = new VuforiaHandler();
        movementHandler = new MovementHandler();

        /* ArrayList<Float> robotXYR = vuforiaHandler.getRobotXYR();
        robotX = robotXYR.get(0); // maybe don't cast to double
        robotY = robotXYR.get(1);
        robotR =  robotXYR.get(2); */

        leftFront = movementHandler.getDcMotor("leftFront");
        rightFront = movementHandler.getDcMotor("rightFront");
        leftRear = movementHandler.getDcMotor("leftRear");
        rightRear = movementHandler.getDcMotor("rightRear");


        restState = new RestState("rest"); // example
        moveToLineExample = new MoveToLocation("moveToLineExample", vuforiaHandler, movementHandler, new double[]{-13.625 * MM_PER_INCH, 8.25 * MM_PER_INCH, -90}, restState);
        stateMachine = new StateMachine("moveToLineExample", restState, moveToLineExample);
    }

    @Override
    public void loop() {
        vuforiaHandler.update();
        stateMachine.run();

        // telemetry
        telemetry.addData("CURRENT STATE", stateMachine.getCurrentState().getName());
        telemetry.addData("MOTOR LF", String.format(Locale.ENGLISH, "POS: %s, POW: %s", leftFront.getTargetPosition(), leftFront.getPower()));
        telemetry.addData("MOTOR RF", String.format(Locale.ENGLISH, "POS: %s, POW: %s", rightFront.getTargetPosition(), rightFront.getPower()));
        telemetry.addData("MOTOR LR", String.format(Locale.ENGLISH, "POS: %s, POW: %s", leftRear.getTargetPosition(), leftRear.getPower()));
        telemetry.addData("MOTOR RR", String.format(Locale.ENGLISH, "POS: %s, POW: %s", rightRear.getTargetPosition(), rightRear.getPower()));
        telemetry.update();
    }
}
