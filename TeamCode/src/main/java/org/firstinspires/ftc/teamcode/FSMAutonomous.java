package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.states.DefaultAuto;

import java.util.Locale;

/* TODO
    - Tune PID
    - Update phone location in VuforiaHandler
    - Standardize variable names
    - Clean MoveAndShoot and wrapped StateMachines
    - Consider a better way to pass handlers (hate to say it but maybe public handlers)
 */

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "FSMAutonomous", group = "Autonomous")
public class FSMAutonomous extends OpMode {
    public VuforiaHandler vuforiaHandler;
    public MovementHandler movementHandler;

    private DefaultAuto defaultAuto;

    private static float MM_PER_INCH = 25.4f;

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

        defaultAuto = new DefaultAuto("default", vuforiaHandler, movementHandler, Side.BLUE); // edit side enum for different sides
        defaultAuto.init(null);
    }

    @Override
    public void loop() {
        vuforiaHandler.update();
        defaultAuto.run();

        // telemetry
        telemetry.addData("CURRENT STATE", defaultAuto.getCurrentState().getName());
        telemetry.addData("MOTOR LF", String.format(Locale.ENGLISH, "POS: %s, POW: %s", leftFront.getTargetPosition(), leftFront.getPower()));
        telemetry.addData("MOTOR RF", String.format(Locale.ENGLISH, "POS: %s, POW: %s", rightFront.getTargetPosition(), rightFront.getPower()));
        telemetry.addData("MOTOR LR", String.format(Locale.ENGLISH, "POS: %s, POW: %s", leftRear.getTargetPosition(), leftRear.getPower()));
        telemetry.addData("MOTOR RR", String.format(Locale.ENGLISH, "POS: %s, POW: %s", rightRear.getTargetPosition(), rightRear.getPower()));
        telemetry.update();
    }
}
