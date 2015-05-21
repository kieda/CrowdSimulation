package edu.cmu.cs464.p3.util;

import com.continuent.tungsten.fsm.core.FiniteStateException;
import com.continuent.tungsten.fsm.core.State;
import com.continuent.tungsten.fsm.core.StateTransitionMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

/**
 * @author zkieda
 */
public class StateTransitionBuilder {
    private final StateTransitionMap map;
    private final Set<State> addedStates;
    
    public StateTransitionBuilder(StateTransitionMap map){
        this.map = map;
        addedStates = new HashSet<>();
    }
    
    private void addState(State s) throws FiniteStateException{
        if(!addedStates.contains(s)){
            addedStates.add(s);
            map.addState(s);
        }
    }
    
    /** 
     * 
     * We structure the generator function this way, such that we can 
     * create SubStates with the same structure under different parents.
     * 
     * @param parent The parent of this subspace. {@code null} if none.
     * @param genFSM Generating fn for this FSM
     * @return The parent state.
     */
    public State gen(State parent, 
            Function<State, State[][]> genFSM) {
        try{
            State[][] transitions = genFSM.apply(parent);
            for(State[] transition : transitions){
                addState(transition[0]);
                addState(transition[1]);
                map.addTransition(new AggregateTransition(transition[0], transition[1]));
            }
            return parent;
        } catch(FiniteStateException fsmException) {
            throw new Error("Error in creating FSM Transition Map"
                + parent == null? "" : " for parent " + parent, fsmException);
        }
    }
    
    public StateTransitionMap getMap() {
        return map;
    }
}
