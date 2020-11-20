package org.firstinspires.ftc.teamcode.depricated;

import org.firstinspires.ftc.teamcode.states.AbState;

@Deprecated
public class StateMachine { // this is unnecessary
    private AbState currentState;

    StateMachine(AbState initialState){
        currentState = initialState;
    }

    public AbState getCurrentState(){
        return currentState;
    }

    public void run(){
        currentState.run();
        currentState = currentState.next(); // this could probably be done better
    }
}
