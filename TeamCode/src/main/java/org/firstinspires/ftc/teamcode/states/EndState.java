package org.firstinspires.ftc.teamcode.states;

public class EndState extends AbState {
    EndState(String name) {
        super(name);
    }

    @Override
    public void init(AbState previousState) {
    }

    @Override
    public AbState next() {
        return this;
    }

    @Override
    public void run() {
        // do nothing
    }
}
