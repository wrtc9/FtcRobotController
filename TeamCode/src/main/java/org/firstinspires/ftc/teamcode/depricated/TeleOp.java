package org.firstinspires.ftc.teamcode.depricated;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.depricated.Moveable;

import java.util.HashMap;
import java.util.function.BiConsumer;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp", group = "TeleOpMode")

@Deprecated
@Disabled
public class TeleOp extends OpMode {

    /*
    TODO
     Consider other motors that will be included in the robot
     */

    private DcMotor leftFront; // Remember to initialize the objects outside of init, this way just makes more sense
    private DcMotor rightFront;
    private DcMotor leftRear;
    private DcMotor rightRear;

    private Moveable<DcMotor, Double> driveTrain;

    private double l, r, s;

    private final double DEADZONE = 0.1;

    public double clip(double p, double d){
        return (p<d && p>-d) ? 0 : p;
    }
    public double clip(double p){
        return clip(p, DEADZONE);
    }
    @Override
    public void init() {

        leftFront = hardwareMap.dcMotor.get("leftFront"); // this could probably be automated
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftRear = hardwareMap.dcMotor.get("leftRear");
        rightRear = hardwareMap.dcMotor.get("rightRear");

        l = clip(gamepad1.left_stick_y);
        r = clip(gamepad1.left_stick_x);
        s = clip(gamepad2.right_stick_x);

        HashMap<DcMotor, Double> driveTrainCoef = new HashMap<>();
        driveTrainCoef.put(leftFront, (l+r+s)/(l+r+s)); // Need to add more, pay attention to the coef
        BiConsumer<DcMotor, Double> dcSetPower = DcMotorSimple::setPower;

        driveTrain = new Moveable<>(driveTrainCoef, dcSetPower); // is generifying necessary?
        /* Since Moveable just runs the forEach command, making a separate object isn't necessary;
        * however, doing so helps out in other areas */

    }

    @Override
    public void loop() {

        driveTrain.run(); // is this efficient?

    }
}
