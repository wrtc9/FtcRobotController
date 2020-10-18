package org.firstinspires.ftc.teamcode;

public abstract class MoveState extends AbState {
    protected VuforiaHandler vuforiaHandler;
    public MoveState(String name, VuforiaHandler vuforiaHandler) {
        super(name);
        this.vuforiaHandler = vuforiaHandler;
    }
    // put useful static methods here

    public static void moveWithEncoders(double linear, double lateral, double rotational){
    }

    //public static double angleTo(){}

}
