package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.ArrayList;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "FSMAutonomous", group = "Autonomous")
public class FSMAutonomous extends OpMode {
    public VuforiaHandler vuforiaHandler;
    public double robotX;
    public double robotY;
    public double robotR;

    @Override
    public void init() {
        // initialize motors
        vuforiaHandler = new VuforiaHandler();
        ArrayList<Float> robotXYR = vuforiaHandler.getRobotXYR();
        robotX = (double) robotXYR.get(0);
        robotY = (double) robotXYR.get(1);
        robotR = (double) robotXYR.get(2);
    }

    @Override
    public void loop() {
        vuforiaHandler.update();
    }
}
