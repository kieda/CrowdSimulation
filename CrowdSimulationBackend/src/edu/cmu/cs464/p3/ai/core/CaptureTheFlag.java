package edu.cmu.cs464.p3.ai.core;


import com.continuent.tungsten.fsm.core.EntityAdapter;
import com.continuent.tungsten.fsm.core.State;
import com.continuent.tungsten.fsm.core.StateMachine;
import com.continuent.tungsten.fsm.core.StateTransitionMap;
import com.continuent.tungsten.fsm.core.StateType;
import edu.cmu.cs464.p3.ai.core.Group;
import edu.cmu.cs464.p3.ai.core.Objective;
import edu.cmu.cs464.p3.ai.core.Objective.ObjectiveStatus;
import edu.cmu.cs464.p3.ai.core.Player;
import edu.cmu.cs464.p3.ai.core.PlayerObjective;

//represents the capture the flag game


// make "objective" DFA --
//  each transition will have an associated payoff value
//  example for capture the flag : 
//   node A : grab enemy flag
//   node B : bring enemy flag back to base

// when the flag carryer has been detroyed while on node B, 
// the DFA retransitions to node A. 

// we also associate other tasks in this DFA. 
// for example, if our flag is taken, we consider a great fear 
// and risk in letting the enemy escape. 

// if our team is taking the enemy flag, we find a greater payoff in 
// helping the flag carryer. 

// for this scenario, each side has a DFA with five nodes
//    A - neither flags taken
//    B - our flag taken, enemy not
//    C - our flag not tken, enemy flag taken
//    D - both flags taken
//    E - exit state (one of the flags made it to the other side)

//of course, for this game, the additional effects are just cumulative based on 
//the status of the flag
//  

// have high level idea of everything
// need to implement specifics
public class CaptureTheFlag implements Objective{
    private final Group group;
    
    public CaptureTheFlag(Group group){
        this.group = group;
    }
    private class CTFPlayerObjective implements PlayerObjective{
        private final Player player;
        private StateTransitionMap internalFSM;
        private StateMachine machine;
        public CTFPlayerObjective(Player player) {
            this.player = player;
            internalFSM = new StateTransitionMap();
            State neitherFlags = new State("noFlagsTaken", StateType.START);
            State enemyFlagTaken = new State("enemyFlagTaken", StateType.ACTIVE);
            State ourFlagTaken = new State("ourFlagTaken", StateType.ACTIVE);
            State bothFlagsTaken = new State("bothFlagsTaken", StateType.ACTIVE);
            State enemyFlagRetrieved = new State("enemyFlagRetrieved", StateType.END);
            State ourFlagRetrieved = new State("ourFlagRetrieved", StateType.END);
            
            machine = new StateMachine(internalFSM, null);
            
        }
        
    }
    
    @Override
    public PlayerObjective makePlayerObjective(Player p) {
        return new CTFPlayerObjective(p);
    }
    
    @Override
    public ObjectiveStatus getObjectiveStatus() {
        if(group.size() == 0) return ObjectiveStatus.FAIL;
        
        return ObjectiveStatus.RUNNING;
    }
}

//todo - make into module
// be able to add gameobjects to a group
// this will give us a way to add in the flag