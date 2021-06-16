package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.handlers.MovementHandler;

@Autonomous(name="Speed Test", group="Autonomous")
public class SpeedTest extends OpMode {

    private MovementHandler movementHandler;
    private final long timer = 1000 * 30;
    private long start;

    @Override
    public void init() {
        movementHandler = new MovementHandler(hardwareMap);
        long start = System.currentTimeMillis();
    }

    @Override
    public void loop() {
        if (System.currentTimeMillis() < start + timer) {
            movementHandler.move(1, 0, 0, 0.5);
        }
        else {
            movementHandler.move(1, 1, 1, 0);
        }
    }
}
