package org.firstinspires.ftc.teamcode.states;

import org.firstinspires.ftc.teamcode.qol.TelemetryInfo;

import java.util.ArrayList;

public abstract class NewAbState {
    protected ArrayList<TelemetryInfo> privateInfo;
    protected NewAbState nextState;
    public NewAbState(ArrayList<TelemetryInfo> info, NewAbState nextState){
        privateInfo.addAll(info);
        this.nextState = nextState;
    }
    public abstract void run();
    public abstract NewAbState next();
}
