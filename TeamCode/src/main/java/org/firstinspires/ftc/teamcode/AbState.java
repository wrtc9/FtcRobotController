package org.firstinspires.ftc.teamcode;

public abstract class AbState { // this is basically a decorator pattern
    protected String name;
    protected AbState currentState = this;

    public AbState(String name){
        this.name = name;
    } // maybe add handlers to constructor

    public String getName() {
        return name;
    }

    public AbState getCurrentState(){
        return currentState;
    }

    public abstract void init(AbState previousState); // expected to be run in next; brug what if we put parameters on init (maybe previous state?)

    public abstract AbState next(); // returns next state to be run and also end behavior

    public abstract void run();
}
