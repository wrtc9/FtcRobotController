package org.firstinspires.ftc.teamcode.handlers;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.qol.SensorDetection;
import org.firstinspires.ftc.teamcode.qol.WobbleSetting;

import java.util.EnumSet;
import java.util.HashMap;

/* TODO
    - Comment
 */

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

public class MovementHandler {

    public enum Mode { // maybe change this to just use FTC's default enums
        RUN_WITH_ENCODERS,
        RUN_WITHOUT_ENCODERS
    }
    private Mode currentOpMode;

    private final DcMotor leftFront;
    private final DcMotor rightFront;
    private final DcMotor leftRear;
    private final DcMotor rightRear;
    private HashMap<String, DcMotor> drivetrain;

    private final DcMotor flywheel;
    private final DcMotor ramp;

    /*private double linEncoderCoef;
    private double latEncoderCoef;
    private double rotEncoderCoef;*/

    public MovementHandler(){
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftRear = hardwareMap.dcMotor.get("leftRear");
        rightRear = hardwareMap.dcMotor.get("rightRear");

        drivetrain.put("leftFront", leftFront);
        drivetrain.put("rightFront", rightFront);
        drivetrain.put("leftRear", leftRear);
        drivetrain.put("rightRear", rightRear);

        flywheel = hardwareMap.dcMotor.get("flywheel");
        ramp = hardwareMap.dcMotor.get("ramp");

        flywheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        ramp.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        /*try {
            FileInputStream fileIn = new FileInputStream("CalibrationInfo");
            ObjectInputStream in = new ObjectInputStream(fileIn);

            ArrayList<CalibrationInfo> calibrationInfo = (ArrayList<CalibrationInfo>) in.readObject();

            CalibrationInfo linInfo = calibrationInfo.get(0);
            CalibrationInfo latInfo = calibrationInfo.get(1);
            CalibrationInfo rotInfo = calibrationInfo.get(2);

            linEncoderCoef = linInfo.getTicksPerMM();
            latEncoderCoef = latInfo.getTicksPerMM();
            rotEncoderCoef = rotInfo.getTicksPerMM();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }*/
    }

    public void move(double lin, double lat, double rot){ // if you know for certain that abs(lin + lat + rot) < 1

        if (currentOpMode != Mode.RUN_WITHOUT_ENCODERS) {
            changeCurrentOpmode(Mode.RUN_WITHOUT_ENCODERS);
        }

        leftFront.setPower(lin - lat - rot);
        rightFront.setPower(lin + lat + rot);
        leftRear.setPower(lin + lat - rot);
        rightRear.setPower(lin - lat + rot);
    }

    public void move(double lin, double lat, double rot, int weight){ // weight is a bit like speed in a sense

        if (currentOpMode != Mode.RUN_WITHOUT_ENCODERS) {
            changeCurrentOpmode(Mode.RUN_WITHOUT_ENCODERS);
        }

        leftFront.setPower(sigmoid(lin - lat - rot, weight));
        rightFront.setPower(sigmoid(lin + lat + rot, weight));
        leftRear.setPower(sigmoid(lin + lat - rot, weight));
        rightRear.setPower(sigmoid(lin - lat + rot, weight));
    }

    public double sigmoid(double x, int weight) { // add in a coefficient of x
        return 2 / (1 + Math.exp(x / weight)) - 1;
    }

    public void changeModes(DcMotor.RunMode runMode){
        for (DcMotor dcMotor : drivetrain.values()) dcMotor.setMode(runMode);
    }

    public void changeCurrentOpmode (Mode currentOpMode){
        if (currentOpMode == Mode.RUN_WITH_ENCODERS){
            changeModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            changeModes(DcMotor.RunMode.RUN_TO_POSITION);
        }
        else {
            changeModes(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }

    /*public void moveWithEncoders(double linMM, double latMM, double rotMM){
        int lin = (int) (linMM * linEncoderCoef);
        int lat = (int) (latMM * latEncoderCoef);
        int rot = (int) (rotMM * rotEncoderCoef);

        moveWithTicks(lin, lat, rot);
    }*/

    public void moveWithTicks(int lin, int lat, int rot){ // this might break
        if (currentOpMode != Mode.RUN_WITH_ENCODERS){
            changeCurrentOpmode(Mode.RUN_WITH_ENCODERS);
        }

        leftFront.setTargetPosition(lin - lat - rot);
        rightFront.setTargetPosition(lin + lat + rot);
        leftRear.setTargetPosition(lin + lat - rot);
        rightRear.setTargetPosition(lin - lat + rot);
    }

    public void shoot(double power, int time) {
        ramp.setPower(1); // when should we turn off motors?
        flywheel.setPower(power);
        try {
            wait(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ramp.setPower(0);
        flywheel.setPower(0);
    }

    public double distanceTo(double x, double y){
        return Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2));
    }

    public double thetaTo(double x, double y, double d){ // d is distanceTo(x, y)
        double rawTheta = Math.toDegrees(Math.acos(x/d));
        return (y > 0) ? rawTheta : -rawTheta;
    }

    public double[] errorTransformer(double x, double y, double r) { // rotates target around robot such that robot is facing forward for lack of a better way of saying it
        // re-work this to return something like a Target
        double d = distanceTo(x, y);
        double theta = thetaTo(x, y, r);
        theta += 90 - r; // where the actual rotating happens
        double newX = Math.cos(theta) * d;
        double newY = Math.sin(theta) * d;
        return new double[] {newX, newY};
    }

    public DcMotor getDcMotor(String name){
        return drivetrain.get(name);
    }

    public boolean isBusy(){
        return leftFront.isBusy() || rightFront.isBusy() || leftRear.isBusy() || rightRear.isBusy();
    }

    public WobbleSetting findWobble() { // could use sensors or tfod with ratio between sides (make sure camera is straight on if doing this)
        return null;
    }

    public EnumSet<SensorDetection> getSensorDetections(float precision) { // set
        return null;
    }
}
