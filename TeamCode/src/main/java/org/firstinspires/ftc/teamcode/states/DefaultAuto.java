package org.firstinspires.ftc.teamcode.states;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.handlers.MovementHandler;
import org.firstinspires.ftc.teamcode.qol.Side;
import org.firstinspires.ftc.teamcode.handlers.VuforiaHandler;
import org.firstinspires.ftc.teamcode.qol.TelemetryInfo;

import java.util.Locale;

public class DefaultAuto extends AbState {

    private AbState moveAndShoot, moveToWobbleZone, end;

    private final TelemetryInfo lfPowerInfo = new TelemetryInfo("LEFT FRONT POWER:"),
                                rfPowerInfo = new TelemetryInfo("RIGHT FRONT POWER:"),
                                lrPowerInfo = new TelemetryInfo("LEFT REAR POWER:"),
                                rrPowerInfo = new TelemetryInfo("RIGHT REAR POWER:"),
                                currentPosInfo = new TelemetryInfo("POSITION:");

    private final DcMotor leftFront, rightFront, leftRear, rightRear;

    private final VuforiaHandler vuforiaHandler;
    private final MovementHandler movementHandler;

    public DefaultAuto(String name, VuforiaHandler vuforiaHandler, MovementHandler movementHandler, Side side) { // idea: put this in a package with the other states
        super(name);
        this.vuforiaHandler = vuforiaHandler;
        this.movementHandler = movementHandler;

        moveAndShoot = new MoveAndShoot("MoveAndShoot", vuforiaHandler, movementHandler, moveToWobbleZone, side); // maybe do moveToWobbleZone first
        moveToWobbleZone = new MoveToWobbleZone("MoveToWobbleZone", vuforiaHandler, movementHandler, side, end);
        end = new EndState("End");
        currentState = moveAndShoot;

        telemetryObjs.add(new TelemetryInfo("SIDE:", side.getName()));
        telemetryObjs.add(lfPowerInfo);
        telemetryObjs.add(rfPowerInfo);
        telemetryObjs.add(lrPowerInfo);
        telemetryObjs.add(rrPowerInfo);
        telemetryObjs.add(currentPosInfo);

        leftFront = movementHandler.getDcMotor("leftFront");
        rightFront = movementHandler.getDcMotor("rightFront");
        leftRear = movementHandler.getDcMotor("leftRear");
        rightRear = movementHandler.getDcMotor("rightRear");
    }

    @Override
    public void init(AbState previousState) {
        movementHandler.changeModes(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public AbState next() {
        if (currentState == end) {
            return new EndState("End"); // for telemetry
        }
        return this;
    }
    // idea: if (currentState != end) { return this; }; this will work better if we want to nest further and should be the preferred nested state next(); this causes issues for this state
    // something special should happen with end and the parent should know (eg: special telemetry for end state; should be the same structure as other nested states)
    // then again, currentState != end should only be used when the end of the state machine is when the final state terminates
    // explore this idea of state machines terminating

    @Override
    public void run() {
        runMachine(); // this is defined in AbState

        lfPowerInfo.setContent(String.valueOf(leftFront.getPower()));
        rfPowerInfo.setContent(String.valueOf(rightFront.getPower()));
        lrPowerInfo.setContent(String.valueOf(leftRear.getPower()));
        rrPowerInfo.setContent(String.valueOf(rightRear.getPower()));

        currentPosInfo.setContent(String.format(Locale.ENGLISH, "X: %f, Y: %f, R: %f", vuforiaHandler.getRobotX(), vuforiaHandler.getRobotY(), vuforiaHandler.getRobotR()));
    }
}
