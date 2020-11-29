package org.firstinspires.ftc.teamcode.states;

import org.firstinspires.ftc.teamcode.qol.TelemetryInfo;

import java.util.ArrayList;

public abstract class AbState { // this is basically a decorator pattern
    protected String name;
    protected AbState currentState = this;
    protected AbState previousState;

    protected ArrayList<TelemetryInfo> telemetryObjs;

    public AbState(String name){
        this.name = name;

        telemetryObjs.add(new TelemetryInfo("STATE:", name));
    } // maybe add handlers to constructor

    public String getName() {
        return name;
    }

    public AbState getCurrentState(){
        return currentState;
    }

    public abstract void init(AbState previousState); // expected to be run in next; brug what if we put parameters on init (maybe previous state?)
    // refactor this to not take previousState in favor of building, see FindZone
    // or not, init should be required after all
    // then again, when initializing there's not much safety in the state being initialized knowing what the passed state actually is
    // maybe run the end check on the outside (will not work for most states)

    public abstract AbState next(); // returns next state to be run and also end behavior

    public abstract void run();

    public ArrayList<TelemetryInfo> getTelemetry() {
        return telemetryObjs;
    }

    protected void runMachine() {
        currentState.run();

        previousState = currentState;
        currentState = currentState.next();

        if (previousState != currentState) {
            currentState.init(previousState);

            telemetryObjs.removeAll(previousState.getTelemetry()); // this will allow intermediate states to add telemetry
            telemetryObjs.addAll(currentState.getTelemetry());
        }
    }
}
