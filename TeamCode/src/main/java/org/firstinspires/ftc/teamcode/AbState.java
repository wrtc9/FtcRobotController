package org.firstinspires.ftc.teamcode;

public abstract class AbState { // this is basically a decorator pattern
    protected String name = "Unnamed";
    protected StateMachine stateMachine;

    public AbState(String name){
        this.name = name;
    }

    public String getName() { return name; }

    public void passMachine(StateMachine machine){ stateMachine = machine; }

    public abstract void run();
}
