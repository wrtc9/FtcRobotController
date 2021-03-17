package org.firstinspires.ftc.teamcode.states;

import org.firstinspires.ftc.teamcode.qol.TelemetryInfo;

import java.util.ArrayList;

public abstract class Head extends NewAbState { // allows for modularity
    public Head(ArrayList<TelemetryInfo> info, NewAbState nextState){
        super(info, nextState);
    }
    @Override
    public void run() {}

    @Override
    public abstract NewAbState next();
}
