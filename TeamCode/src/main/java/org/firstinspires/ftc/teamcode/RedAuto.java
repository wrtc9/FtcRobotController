package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.qol.Side;

@Autonomous(name="RedAuto", group="Autonomous")
public class RedAuto extends FSMAutonomous {
    @Override
    protected Side getSide() {
        return Side.RED;
    }
}
