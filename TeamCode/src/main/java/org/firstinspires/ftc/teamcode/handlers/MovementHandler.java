package org.firstinspires.ftc.teamcode.handlers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Hardware;

import org.firstinspires.ftc.teamcode.qol.SensorDetection;
import org.firstinspires.ftc.teamcode.qol.WobbleSetting;

import java.util.EnumSet;
import java.util.HashMap;

/* TODO
    - Comment
 */

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

/**
 * The MovementHandler class provides a central location for all movement-related as well as a
 * couple data-related methods. Currently, MovementHandler is instantiated, but in the future,
 * functionality could be made static.
 *
 * @author Will (wrtc9)
 */
public class MovementHandler { // make this static

    private DcMotor.RunMode currentMode;

    private final DcMotor leftFront;
    private final DcMotor rightFront;
    private final DcMotor leftRear;
    private final DcMotor rightRear;
    private final HashMap<String, DcMotor> drivetrain;

    private final DcMotor flywheel;
    private final DcMotor ramp;

    /*private double linEncoderCoef;
    private double latEncoderCoef;
    private double rotEncoderCoef;*/

    public MovementHandler(HardwareMap devices){ // this will turn into a static block
        leftFront = devices.dcMotor.get("leftFront");
        rightFront = devices.dcMotor.get("rightFront");
        leftRear = devices.dcMotor.get("leftRear");
        rightRear = devices.dcMotor.get("rightRear");

        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightRear.setDirection(DcMotorSimple.Direction.REVERSE);

        drivetrain = new HashMap<String, DcMotor>();
        drivetrain.put("leftFront", leftFront);
        drivetrain.put("rightFront", rightFront);
        drivetrain.put("leftRear", leftRear);
        drivetrain.put("rightRear", rightRear);

        flywheel = devices.dcMotor.get("flywheel");
        ramp = devices.dcMotor.get("ramp");

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

    public void move(double lin, double lat, double rot, double speed){ // though both move methods would work, this one might be better for TeleOp

        if (currentMode != DcMotor.RunMode.RUN_WITHOUT_ENCODER) {
            changeCurrentMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        double total = Math.abs(lin) + Math.abs(lat) + Math.abs(rot);

        leftFront.setPower((lin - lat - rot)/total*speed); // let's check this
        rightFront.setPower((lin + lat + rot)/total*speed);
        leftRear.setPower((lin + lat - rot)/total*speed);
        rightRear.setPower((lin - lat + rot)/total*speed);
    }

    public void test(){
        leftFront.setPower(1);
        rightFront.setPower(1);
        leftRear.setPower(1);
        rightRear.setPower(1);
    }
    /*
    /**
     * Move sets the powers of all drive train motors using the parameters lin, lat, rot, and
     * weight. Lin, lat, and rot are summed up (with positive or negative coefficients based on the
     * wheels) and multiplied by the <a href="https://en.wikipedia.org/wiki/Sigmoid_function">sigmoid</a>
     * of the total (sigmoid is translated to range from -1 to 1).
     *
     *
     * @param lin linear component of the robot speed
     * @param lat lateral component of the robot speed
     * @param rot rotational component of the robot speed
     * @param weight weights sigmoid; input to sigmoid function is divided by weight
     */
    /*
    @Deprecated
    public void move(double lin, double lat, double rot, int weight){ // weight is a bit like speed in a sense

        if (currentMode != DcMotor.RunMode.RUN_WITHOUT_ENCODER) {
            changeCurrentMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        double total = lin + lat + rot;

        leftFront.setPower(sigmoid(total, weight) * ((lin - lat - rot) / total));
        rightFront.setPower(sigmoid(total, weight) * ((lin + lat + rot) / total));
        leftRear.setPower(sigmoid(total, weight) * ((lin + lat - rot) / total));
        rightRear.setPower(sigmoid(total, weight) * ((lin - lat + rot) / total));
    }

    public double sigmoid(double x, int weight) {
        return 2 / (1 + Math.exp(-x / weight)) + 1;
    }*/ // make sure power can actually be negative

    public void changeModes(DcMotor.RunMode runMode){
        for (DcMotor dcMotor : drivetrain.values()) dcMotor.setMode(runMode);
    }

    public void changeCurrentMode (DcMotor.RunMode mode){
        if (mode == DcMotor.RunMode.RUN_USING_ENCODER) {
            changeModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
        changeModes(mode);
        currentMode = mode;
    }

    /*public void moveWithEncoders(double linMM, double latMM, double rotMM){
        int lin = (int) (linMM * linEncoderCoef);
        int lat = (int) (latMM * latEncoderCoef);
        int rot = (int) (rotMM * rotEncoderCoef);

        moveWithTicks(lin, lat, rot);
    }*/

    public void moveWithTicks(int lin, int lat, int rot){ // this might break
        if (currentMode != DcMotor.RunMode.RUN_USING_ENCODER){ // this should probably be removed
            changeCurrentMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        leftFront.setTargetPosition((lin - lat - rot)/3); // let's see if dividing by three works well!
        rightFront.setTargetPosition((lin + lat + rot)/3);
        leftRear.setTargetPosition((lin + lat - rot)/3);
        rightRear.setTargetPosition((lin - lat + rot)/3);
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

    /**
     * ErrorTransformer converts a set of coordinates to polar and then adds the complement of r.
     * Practically, this is used to rotate a target point around the robot so the robot is virtually
     * facing towards positive y. This, fixes an issue with x and y error.
     *
     *
     * @param x x distance between robot and target
     * @param y y distance between robot and target
     * @param r rotation of the robot
     */
    public double[] errorTransformer(double x, double y, double r) { // converts to polar, adds to theta, converts back
        // re-work this to return something like a Target
        double d = distanceTo(x, y);
        double theta = thetaTo(x, y, d);
        theta += 90 - r; // where the actual rotating happens
        double newX = Math.cos(Math.toRadians(theta)) * d;
        double newY = Math.sin(Math.toRadians(theta)) * d;
        return new double[] {newX, newY};
    }

    public DcMotor getDcMotor(String name){
        return drivetrain.get(name);
    }

    public boolean isBusy(){
        return leftFront.isBusy() || rightFront.isBusy() || leftRear.isBusy() || rightRear.isBusy();
    }

    public double getWobbleSensor() { // could use sensors or tfod with ratio between sides (make sure camera is straight on if doing this)
        return 0;
    }

    /**
     * GetSensorDetections finds which sensors detect an object within a distance of the variable
     * precision.
     *
     *
     * @param precision how close an object must be before it is avoided
     */
    public EnumSet<SensorDetection> getSensorDetections(float precision) { // set
        return EnumSet.noneOf(SensorDetection.class);
    }

    public int[] getTicks() {
        return new int[] {leftFront.getCurrentPosition(), rightFront.getCurrentPosition(), leftRear.getCurrentPosition(), rightRear.getCurrentPosition()};
    }
}
