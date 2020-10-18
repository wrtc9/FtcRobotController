package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Autonomous", group = "Linear OpMode")
public class LinearAutonomous extends LinearOpMode {

    private DcMotor leftFront;
    private DcMotor rightFront;
    private DcMotor leftRear;
    private DcMotor rightRear;

    private double robotX = 0;
    private double robotY = 0;
    private double robotR = 0;

    private double l = 0;
    private double r = 0;
    private double s = 0;

    private final int ANGLE_PRECISION = 5;
    private final int DISTANCE_PRECISION = 100;

    private HashMap<String, Double[]> locations = new HashMap<>(); // add locations to this and also add safety for the length of the primitive array

    private Moveable<DcMotor, Double> driveTrain;

    // the amount of methods might be unnecessary

    public double distanceTo(double deltaX, double deltaY){ return Math.sqrt(Math.pow(deltaX, 2)+Math.pow(deltaY, 2)); } // pythagorean theorem to get distance to location

    public double thetaTo(double deltaX, double deltaY) { // trig to get shortest angle to location
        double r = distanceTo(deltaX, deltaY);
        return ((deltaY > 0) ? Math.toDegrees(Math.acos(deltaX/r)) : -Math.toDegrees(Math.acos(deltaX/r)));
    }

    public void rotateTo(double degrees){ // rotate until near the angle
        double deltaTheta = 0;
        do {
            deltaTheta = degrees - robotR;
            r = Math.signum(deltaTheta);
            driveTrain.run();
        } while (Math.abs(deltaTheta) > ANGLE_PRECISION);
        r = 0;
        driveTrain.run();
    }

    public void rotateTo(double deltaX, double deltaY){ rotateTo(thetaTo(deltaX, deltaY)); } // rotateTo using location

    public void forwardTo(double deltaX, double deltaY){ // move until you're near the location
        double d = 0;
        do {
            d = distanceTo(deltaX, deltaY);
            l = Math.signum(d);
            driveTrain.run();
        } while (Math.abs(d) > DISTANCE_PRECISION);
        l = 0;
        driveTrain.run();
    }

    public void moveTo(double x, double y){ // rotate and then move to location
        double deltaX = x - robotX;
        double deltaY = y - robotY;
        rotateTo(deltaX, deltaY);
        forwardTo(deltaX, deltaY);
    }

    public void moveTo(String location){ moveTo(locations.get(location)[0], locations.get(location)[1]); } // use location name instead of coordinates


    @Override
    public void runOpMode() throws InterruptedException {
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftRear = hardwareMap.dcMotor.get("leftRear");
        rightRear = hardwareMap.dcMotor.get("rightRear");

        VuforiaHandler vuforiaHandler = new VuforiaHandler();

        ArrayList<Float> robotXYR = vuforiaHandler.getRobotXYR();
        robotX = (double) robotXYR.get(0);
        robotY = (double) robotXYR.get(1);
        robotR = (double) robotXYR.get(2);

        HashMap<DcMotor, Double> driveTrainCoef = new HashMap<>();
        driveTrainCoef.put(leftFront, (l+r+s)/(l+r+s)); // this is just an example, add more eventually
        BiConsumer<DcMotor, Double> dcSetPower = DcMotorSimple::setPower;

        driveTrain = new Moveable<>(driveTrainCoef, dcSetPower);
    }
}

