package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.handlers.MovementHandler;
import org.firstinspires.ftc.teamcode.handlers.VuforiaHandler;
import org.firstinspires.ftc.teamcode.qol.Location;
import org.firstinspires.ftc.teamcode.qol.TelemetryInfo;
import org.firstinspires.ftc.teamcode.states.AbState;
import org.firstinspires.ftc.teamcode.states.EndState;
import org.firstinspires.ftc.teamcode.states.MoveWithPID;

import java.util.ArrayList;

@Autonomous (name="Test", group="Autonomous")
public class Test  extends OpMode {
    private MoveWithPID moveState;
    private AbState currentState;
    private VuforiaHandler vuforiaHandler;
    private MovementHandler movementHandler;

    @Override
    public void init() {
        vuforiaHandler = new VuforiaHandler();
        movementHandler = new MovementHandler(hardwareMap);
        moveState = new MoveWithPID("test", vuforiaHandler, movementHandler, new Location(0, 0, 0), new EndState("end"));
        currentState = moveState;
    }

    @Override
    public void loop() {
        vuforiaHandler.update();
        currentState.run();

        ArrayList<TelemetryInfo> telemetryObjs = currentState.getTelemetry();
        for (TelemetryInfo piece : telemetryObjs){
            telemetry.addData(piece.getCaption(), piece.getContent());
        }

        currentState = currentState.next();
    }
}
