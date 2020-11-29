package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.qol.Side;

@Autonomous (name="BlueAuto", group="Autonomous")
public class BlueAuto extends FSMAutonomous {
    @Override
    protected Side getSide() {
        return Side.BLUE;
    }
}
