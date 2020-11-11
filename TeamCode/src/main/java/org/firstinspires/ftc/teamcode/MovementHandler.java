package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.depricated.CalibrationInfo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

/* TODO
    - Comment
 */

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

public class MovementHandler {

    public enum OpModeMode {
        AUTONOMOUS,
        TELEOP
    }
    private OpModeMode currentOpMode;

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


        if (currentOpMode != OpModeMode.TELEOP) {
            changeCurrentOpmode(OpModeMode.TELEOP);
        }

        leftFront.setPower((lin - lat - rot)/total);
        rightFront.setPower((lin + lat + rot)/total);
        leftRear.setPower((lin + lat - rot)/total);
        rightRear.setPower((lin - lat + rot)/total);
    }

    public void changeModes(DcMotor.RunMode runMode){
        for (DcMotor dcMotor : dcMotors.values()) dcMotor.setMode(runMode);
    }

    public void changeCurrentOpmode (OpModeMode currentOpMode){
        if (currentOpMode == OpModeMode.AUTONOMOUS){
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
        if (currentOpMode != OpModeMode.AUTONOMOUS){
            changeCurrentOpmode(OpModeMode.AUTONOMOUS);
        }

        leftFront.setTargetPosition(lin - lat - rot);
        rightFront.setTargetPosition(lin + lat + rot);
        leftRear.setTargetPosition(lin + lat - rot);
        rightRear.setTargetPosition(lin - lat + rot);
    }

    public void shoot(){
        // shoots
    }

    public double distanceTo(double deltaX, double deltaY){
        return Math.sqrt(Math.pow(deltaX, 2)+Math.pow(deltaY, 2));
    }

    public double thetaTo(double x, double y){
        double r = distanceTo(x, y);
        return ((y > 0) ? Math.toDegrees(Math.acos(x/r)) : -Math.toDegrees(Math.acos(x/r)));
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
}
