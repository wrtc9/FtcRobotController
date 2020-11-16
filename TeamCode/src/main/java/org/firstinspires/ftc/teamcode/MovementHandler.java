package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/* TODO
    - Comment
 */

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

public class MovementHandler {

    public enum Mode {
        AUTONOMOUS,
        TELEOP
    }
    private Mode currentOpMode;

    private DcMotor leftFront;
    private DcMotor rightFront;
    private DcMotor leftRear;
    private DcMotor rightRear;
    private HashMap<String, DcMotor> dcMotors;

    /*private double linEncoderCoef;
    private double latEncoderCoef;
    private double rotEncoderCoef;*/

    public MovementHandler(){ // takes vuforiahandler?
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftRear = hardwareMap.dcMotor.get("leftRear");
        rightRear = hardwareMap.dcMotor.get("rightRear");

        dcMotors.put("leftFront", leftFront);
        dcMotors.put("rightFront", rightFront);
        dcMotors.put("leftRear", leftRear);
        dcMotors.put("rightRear", rightRear);

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

    public void move(double lin, double lat, double rot){
        double total = lin + lat + rot;


        if (currentOpMode != Mode.TELEOP) {
            changeCurrentOpmode(Mode.TELEOP);
        }

        leftFront.setPower((lin - lat - rot)/total);
        rightFront.setPower((lin + lat + rot)/total);
        leftRear.setPower((lin + lat - rot)/total);
        rightRear.setPower((lin - lat + rot)/total);
    }

    public void changeModes(DcMotor.RunMode runMode){
        for (DcMotor dcMotor : dcMotors.values()) dcMotor.setMode(runMode);
    }

    public void changeCurrentOpmode (Mode currentOpMode){
        if (currentOpMode == Mode.AUTONOMOUS){
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
        if (currentOpMode != Mode.AUTONOMOUS){
            changeCurrentOpmode(Mode.AUTONOMOUS);
        }

        leftFront.setTargetPosition(lin - lat - rot);
        rightFront.setTargetPosition(lin + lat + rot);
        leftRear.setTargetPosition(lin + lat - rot);
        rightRear.setTargetPosition(lin - lat + rot);
    }

    public void shoot(){
        // shoots
    }

    public double distanceTo(double x, double y){
        return Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2));
    }

    public double thetaTo(double x, double y, double d){ // d is distanceTo(x, y)
        double rawTheta = Math.toDegrees(Math.acos(x/d));
        return (y > 0) ? rawTheta : -rawTheta;
    }

    public double[] errorTransformer(double x, double y, double r) { // rotates target around robot such that robot is facing forward for lack of a better way of saying it
        // (if we didn't account for rotation, PIDs would not work; e.g. robot does not face the target)
        double d = distanceTo(x, y);
        double theta = thetaTo(x, y, r);
        theta += 90 - r; // where the actual rotating happens
        double newX = Math.cos(theta) * d;
        double newY = Math.sin(theta) * d;
        return new double[] {newX, newY};
    }

    public DcMotor getDcMotor(String name){
        return dcMotors.get(name);
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
