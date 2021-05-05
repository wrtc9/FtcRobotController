package org.firstinspires.ftc.teamcode.handlers;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.qol.TelemetryInfo;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;
import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;

/**
 * Class VuforiaHandler handles everything that is related to Vuforia. Check the {@link org.firstinspires.ftc.robotcontroller.external.samples.ConceptVuforiaUltimateGoalNavigation FTC example} which
 * this heavily takes after to learn more. Currently, Vuforia is instantiated, but in the future it
 * could be made static, like MovementHandler.
 *
 * @author Will Terrell (wrtc9)
 */
public class VuforiaHandler { // 0, 0, 0 is the middle of the field looking at blue alliance target
    private final static float mmPerInch = 25.4f; //constants and stuff
    private final static float targetHeight = 6 * mmPerInch;

    private static final float halfField = 72 * mmPerInch;
    private static final float quadField  = 36 * mmPerInch;

    private static final VuforiaLocalizer.CameraDirection CAMERA_DIRECTION = BACK;
    private static final boolean PHONE_IS_POTRAIT = false;

    private static final String VUFORIA_KEY =
            "AbHWAiD/////AAABmS6m9L9soEWjpzBuvqk9/3+Kd0w2QaDoRw6cDQsTZuCgweEdqy+yEon0insMwu3TvrUlrnl4gF4BahTHBEjpxPXieLmXn0Zkpyn6+k2tB0DPcLHDb326xE2IO8SUVCYTB+oVg7Hm1WmZIGjIjaKCEw0hRF8G+PgpeK9vL5ddL6HuUWavzFRaRIFdzhu7ZBd8W2lsWerDqD6luTkIOv661M5U7BNAqD73cFZiLnqrOM3dSJ1wRDpl7XmZJ+9NkWiFVBPUfmOqXebkBax/ti/IALf3X7HgsQ92ckC9aPDOoZdI5t1fotYqTRRgXf/odIYrnReHEE0ud+44/5kkhSfOBV+W54Im2gFqtVgzTrGsQKS4";

    private OpenGLMatrix lastLocation = null;
    private VuforiaLocalizer vuforia = null;
    private boolean targetVisible = false;
    private float phoneXRotate    = 0;
    private float phoneYRotate    = 0;
    private float phoneZRotate    = 0;

    private float robotX = 0;
    private float robotY = 0;
    private float robotR = 0;

    private List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();

    private ArrayList<TelemetryInfo> telemetryObjs = new ArrayList<TelemetryInfo>();

    public float getRobotX(){
        return robotX;
    }
    public float getRobotY(){
        return robotY;
    }
    public float getRobotR(){
        return robotR;
    }

    public VuforiaHandler(HardwareMap devices) {

        int cameraMonitorViewId = devices.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", devices.appContext.getPackageName()); // vuforia init stuff
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection   = CAMERA_DIRECTION;

        parameters.useExtendedTracking = false;

        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        VuforiaTrackables targetsUltimateGoal = this.vuforia.loadTrackablesFromAsset("UltimateGoal");

        allTrackables.addAll(targetsUltimateGoal); // this is for iteration

        // this is just getting it so we can refer to a specific trackable in the trackables obj w/o having to use the get every time (remember these objects hold references to objects inside trackables)
        VuforiaTrackable blueTowerGoalTarget = targetsUltimateGoal.get(0);
        blueTowerGoalTarget.setName("Blue Tower Goal Target");
        VuforiaTrackable redTowerGoalTarget = targetsUltimateGoal.get(1);
        redTowerGoalTarget.setName("Red Tower Goal Target");
        VuforiaTrackable redAllianceTarget = targetsUltimateGoal.get(2);
        redAllianceTarget.setName("Red Alliance Target");
        VuforiaTrackable blueAllianceTarget = targetsUltimateGoal.get(3);
        blueAllianceTarget.setName("Blue Alliance Target");
        VuforiaTrackable frontWallTarget = targetsUltimateGoal.get(4);
        frontWallTarget.setName("Front Wall Target");

        // describe target locations (it would be pretty stupid to init this with a for loop)
        redAllianceTarget.setLocation(OpenGLMatrix
                .translation(0, -halfField, targetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 180)));
        blueAllianceTarget.setLocation(OpenGLMatrix
                .translation(0, halfField, targetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 0)));
        frontWallTarget.setLocation(OpenGLMatrix
                .translation(-halfField, 0, targetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0 , 90)));
        blueTowerGoalTarget.setLocation(OpenGLMatrix
                .translation(halfField, quadField, targetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0 , -90)));
        redTowerGoalTarget.setLocation(OpenGLMatrix
                .translation(halfField, -quadField, targetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90)));

        // camera location stuff, not super complicated
        if (CAMERA_DIRECTION == BACK) {
            phoneYRotate = -90;
        } else {
            phoneYRotate = 90;
        }

        final float CAMERA_FORWARD_DISPLACEMENT  = 4.0f * mmPerInch;
        final float CAMERA_VERTICAL_DISPLACEMENT = 8.0f * mmPerInch;
        final float CAMERA_LEFT_DISPLACEMENT     = 0;

        OpenGLMatrix robotFromCamera = OpenGLMatrix
                .translation(CAMERA_FORWARD_DISPLACEMENT, CAMERA_LEFT_DISPLACEMENT, CAMERA_VERTICAL_DISPLACEMENT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, YZX, DEGREES, phoneYRotate, phoneZRotate, phoneXRotate));

        for (VuforiaTrackable trackable : allTrackables){
            // do this to all trackables
            ((VuforiaTrackableDefaultListener) trackable.getListener()).setPhoneInformation(robotFromCamera, parameters.cameraDirection);
        }

        targetsUltimateGoal.activate();
    }

    public void update() {
        telemetryObjs = new ArrayList<TelemetryInfo>();
        targetVisible = false; // are any targets visible?
        for (VuforiaTrackable trackable : allTrackables) {
            if (((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible()) {
                telemetryObjs.add(new TelemetryInfo("Visible Target", trackable.getName()));
                targetVisible = true; // there's a target visible

                OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener)trackable.getListener()).getUpdatedRobotLocation(); // find the updated location
                if (robotLocationTransform != null) {
                    lastLocation = robotLocationTransform; // if we haven't stayed still, update the location
                }
                break;
            }
        }

        if (targetVisible) { // if there's a target visible, do telemetry stuff

            VectorF translation = lastLocation.getTranslation();
            telemetryObjs.add(new TelemetryInfo("Pos (in)", String.format(Locale.ENGLISH, "{X, Y, Z} = %.1f, %.1f, %.1f",
                    translation.get(0) / mmPerInch, translation.get(1) / mmPerInch, translation.get(2) / mmPerInch)));


            Orientation rotation = Orientation.getOrientation(lastLocation, EXTRINSIC, XYZ, DEGREES);
            telemetryObjs.add(new TelemetryInfo("Rot (deg)",
                    String.format(Locale.ENGLISH, "{Roll, Pitch, Heading} = %.0f, %.0f, %.0f", rotation.firstAngle, rotation.secondAngle, rotation.thirdAngle)));

            // actual autonomous stuff here
            float[] coordinates = lastLocation.getTranslation().getData(); // the fact that we don't know the location always could pose a problem
            robotX = coordinates[0]; // bruh moment this won't work since immutable shit
            robotY = coordinates[1]; // maybe fix with having an object to store update versions of these or have update return something or just run getters everytime but thats a pain in the ass
            robotR = Orientation.getOrientation(lastLocation, EXTRINSIC, XYZ, DEGREES).thirdAngle;
        }
        else { // else, tell telemetry no targets are visible
            telemetryObjs.add(new TelemetryInfo("Visible Target", "none"));
        }
    }

    public ArrayList<TelemetryInfo> getTelemetryObjs() {
        return telemetryObjs;
    }

    public boolean isTargetVisible() {return targetVisible;}
}
