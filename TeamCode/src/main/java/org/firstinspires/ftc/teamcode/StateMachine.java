package org.firstinspires.ftc.teamcode;

import java.util.HashMap;

public class StateMachine {
    private HashMap<String, AbState> stateList = new HashMap();
    private AbState currentState;

    public StateMachine(String initialState, AbState ... states){ // I have no idea what a rest parameter is
        for (AbState state : states) {
            stateList.put(state.getName(), state);
            state.passMachine(this);
        }
        currentState = stateList.get(initialState); // this could be done better I guess
    }

    public AbState getState(String name){ return stateList.get(name); } // if name isn't a key, does it throw an error or do I have to do this myself?

    public AbState getCurrentState(){ return currentState; }

    public void switchStates(AbState state){ currentState = state; } // add telemetry for this maybe?

    public void run(){ currentState.run(); }
}
