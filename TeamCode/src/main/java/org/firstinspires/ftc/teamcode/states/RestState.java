package org.firstinspires.ftc.teamcode.states;

import org.firstinspires.ftc.teamcode.AbState;

public class RestState extends AbState {
    RestState(String name) {
        super(name);
    }

    @Override
    public void init() {

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
