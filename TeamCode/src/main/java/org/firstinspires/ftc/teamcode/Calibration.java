package org.firstinspires.ftc.teamcode;


// repeatedly move with encoders and check against the distance (total ticks/total distance = ticks/mm)
// use serialization?
// if we use encoders the fsm idea becomes a bit clearer

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

// to update serialized objects, deserialize and then add the new stuff
/* TODO
    - Clean up in general
    - Find a better solution for tracking deltaTheta
    - Remove dependency on MovementHandler maybe
    - make it better :(
 */
public class Calibration extends LinearOpMode { // this is not going to work, and I know it's not going to work, but I don't care enough to make it better
    public VuforiaHandler vuforiaHandler;
    public MovementHandler movementHandler;

    public ArrayList<CalibrationInfo> calibrationInfo;

    public CalibrationInfo latCalibration;
    public CalibrationInfo linCalibration;
    public CalibrationInfo rotCalibration;

    public double totalLat = 0;
    public double totalLin = 0;
    public double totalRot = 0;
    public int totalTicks = 0;

    public Float robotX;
    public Float robotY;
    public Float robotR;

    public static int DEFAULT_TICKS = 1440;

    public double pythagorean(float x, float y){ // we can replace this with movement handler's distanceTo
        return Math.sqrt(x*x + y*y);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        vuforiaHandler = new VuforiaHandler();
        movementHandler = new MovementHandler();

        ArrayList<Float> robotXYR = vuforiaHandler.getRobotXYR();
        robotX = robotXYR.get(0);
        robotY = robotXYR.get(1);
        robotR = robotXYR.get(2);

        while (opModeIsActive()){ // this can definitely be done better :(
            float[] linInital = {robotX, robotY};
            movementHandler.moveWithTicks(DEFAULT_TICKS, 0, 0);
            while (movementHandler.isBusy()){
                vuforiaHandler.update();
            }
            totalLin = totalLin + (pythagorean(linInital[0]-robotX, linInital[1]-robotY));

            float[] latInital = {robotX, robotY};
            movementHandler.moveWithTicks(0, DEFAULT_TICKS, 0);
            while (movementHandler.isBusy()){
                vuforiaHandler.update();
            }
            totalLat = totalLat + (pythagorean(latInital[0]-robotX, latInital[1]-robotY));

            float rInital = robotR;
            movementHandler.moveWithTicks(0, 0, DEFAULT_TICKS);
            while (movementHandler.isBusy()){
                vuforiaHandler.update();
            }
            totalRot = totalRot + (robotR-rInital);
            totalTicks = totalTicks + DEFAULT_TICKS;
        }

        try { // serialization hurts
            FileInputStream fileIn = new FileInputStream("CalibrationStorage.txt");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            calibrationInfo = (ArrayList<CalibrationInfo>) in.readObject();
            in.close();
            fileIn.close();

            latCalibration = calibrationInfo.get(0);
            linCalibration = calibrationInfo.get(1);
            rotCalibration = calibrationInfo.get(2);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) { // this might be the wrong exception, we are looking for the object not being found
            calibrationInfo = new ArrayList<CalibrationInfo>();
            latCalibration = new CalibrationInfo();
            linCalibration = new CalibrationInfo();
            rotCalibration = new CalibrationInfo();
        }

        latCalibration.addTicks(totalTicks);
        latCalibration.addDistance(totalLat);

        linCalibration.addTicks(totalTicks);
        linCalibration.addDistance(totalLin);

        rotCalibration.addTicks(totalTicks);
        rotCalibration.addDistance(totalRot);

        try {
            FileOutputStream fileOut = new FileOutputStream("CalibrationStorage.txt");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(calibrationInfo);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
