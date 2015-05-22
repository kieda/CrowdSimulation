package edu.cmu.cs464.p3.ai.core;



import com.continuent.tungsten.fsm.core.FiniteStateException;
import com.continuent.tungsten.fsm.core.State;
import com.continuent.tungsten.fsm.core.StateMachine;
import com.continuent.tungsten.fsm.core.StateTransitionMap;
import com.continuent.tungsten.fsm.core.StateType;
import edu.cmu.cs464.p3.ai.core.Objective.ObjectiveStatus;
import edu.cmu.cs464.p3.util.StateTransitionBuilder;
import java.util.function.Function;

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
public class CaptureTheFlag implements Objective {
    private final Group group;
    
    private final StateTransitionMap groupStateMap;
    private final StateMachine groupMachine;
    
    public static final String STATE_NO_FLAGS_TAKEN = "noFlagsTaken";
    
    public static final String STATE_OUR_FLAG_CAPTURED = "ourFlagCaptured";
    public static final String STATE_ENEMY_FLAG_CAPTURED = "enemyFlagCaptured";
    public static final String STATE_NO_FLAGS_CAPTURED = "noFlagCaptured";
    
    public static final String STATE_BOTH_FLAGS_TAKEN = "bothFlagsTaken";
    public static final String STATE_OUR_FLAG_TAKEN = "ourFlagTaken";
    public static final String STATE_OUR_FLAG_NOT_TAKEN = "ourFlagNotTaken";
    public static final String STATE_ENEMY_FLAG_TAKEN = "enemyFlagTaken";
    public static final String STATE_ENEMY_FLAG_NOT_TAKEN = "enemyFlagNotTaken";
    
    public static final String STATE_PLAYER_DONE = "playerDone";
    public static final String STATE_PLAYER_HOLDING_FLAG = "playerHoldingFlag";
    public static final String STATE_PLAYER_NOT_HOLDING_FLAG = "playerNotHoldingFlag";
    
    public static final String STATE_PLAYER_BEING_ATTACKED = "playerBeingAttacked";
    public static final String STATE_PLAYER_NOT_BEING_ATTACKED = "playerNotBeingAttacked";
    public static final String STATE_PLAYER_ATTACKING = "playerAttacking";
    public static final String STATE_PLAYER_NOT_ATTACKING = "playerNotAttacking";
    
    //function that builds states for when our flag is and is not taken.
    private static State[][] ourFlagTakenFn(State ourFlagTakenParent) { 
        State ourFlagTaken = new State(STATE_OUR_FLAG_TAKEN, StateType.ACTIVE, ourFlagTakenParent);
        State ourFlagNotTaken = new State(STATE_OUR_FLAG_NOT_TAKEN, 
            ourFlagTakenParent.getName().endsWith(STATE_ENEMY_FLAG_NOT_TAKEN) ? 
                    StateType.START : StateType.ACTIVE, ourFlagTakenParent);
        return new State[][]{
            {ourFlagNotTaken, ourFlagTaken},
            {ourFlagTaken, ourFlagNotTaken}
        };
    }
    
    //function that defines the states for 
    //      (our flag taken | our flag not taken) x (enemy flag taken | enemy flag not taken)
    private static Function<State, State[][]>  flagTakenFn(StateTransitionBuilder stateBuilder) { 
        return parent -> {
            State enemyFlagTaken = stateBuilder.gen(new State(STATE_ENEMY_FLAG_TAKEN, StateType.ACTIVE, parent), CaptureTheFlag::ourFlagTakenFn);
            State enemyFlagNotTaken = stateBuilder.gen(new State(STATE_ENEMY_FLAG_NOT_TAKEN, StateType.ACTIVE, parent), CaptureTheFlag::ourFlagTakenFn);
            return new State[][]{
                {enemyFlagTaken, enemyFlagNotTaken},
                {enemyFlagNotTaken, enemyFlagTaken}
            };
        };
    }
    
    
    /**
     * postFix(builder) (pf) defines function that defines the states for 
     *    builder |- (player attacking | player not attacking) x pf
     * Where pf is a set of substates under both of these parent states.
     */
    private static Function<Function<State, State[][]>, Function<State, State[][]>> prefixIsBeingAttacked(StateTransitionBuilder builder){
        return postFixFn -> parent -> {
            State beingAttacked = builder.gen(new State(STATE_PLAYER_BEING_ATTACKED, StateType.ACTIVE, parent), postFixFn);
            State notBeingAttacked = builder.gen(new State(STATE_PLAYER_NOT_BEING_ATTACKED, StateType.ACTIVE, parent), postFixFn);
            
            //we can go from being attacked to not being attacked
            //and back
            return new State[][]{
                {beingAttacked, notBeingAttacked},
                {notBeingAttacked, beingAttacked}
            };
        };
    }
    
    private static final Function<StateTransitionBuilder, Function<String, State>> findChildOf = 
            builder -> str -> builder.getStates().stream().filter(c -> 
            c.getName().endsWith(str)).findAny().get();
    
    public CaptureTheFlag(Group group) {
        this.group = group;
        
        groupStateMap = new StateTransitionMap();
        
        StateTransitionBuilder builder = new StateTransitionBuilder(groupStateMap);
        
        //generates possible states used by this group. This state is omniscient,
        //and follows exactly according to the global state.
        builder.gen(null, 
            parent -> {
                //add possible states while flag is not captured
                State notCaptured = builder.gen(new State(STATE_NO_FLAGS_CAPTURED, StateType.ACTIVE, parent), flagTakenFn(builder));
                
                //states that represent enemy flag being captured, our flag being captured
                State enemyFlagCaptured = new State(STATE_ENEMY_FLAG_CAPTURED, StateType.END, parent);
                State ourFlagCaptured = new State(STATE_OUR_FLAG_CAPTURED, StateType.END, parent);
                
                Function<String, State> findChild = findChildOf.apply(builder);
                State enemyFlagTaken = findChild.apply(STATE_ENEMY_FLAG_TAKEN + ":" + STATE_OUR_FLAG_NOT_TAKEN);
                State ourFlagTaken = findChild.apply(STATE_ENEMY_FLAG_NOT_TAKEN + ":" + STATE_OUR_FLAG_TAKEN);
                State bothFlagsTaken = findChild.apply(STATE_ENEMY_FLAG_TAKEN + ":" + STATE_OUR_FLAG_TAKEN);

                
                return new State[][]{
                    {enemyFlagTaken, enemyFlagCaptured},
                    {ourFlagTaken,   ourFlagCaptured},
                    {bothFlagsTaken, ourFlagCaptured},
                    {bothFlagsTaken, enemyFlagCaptured}
                };
            });
        
        try{
            groupStateMap.build();
        }catch(FiniteStateException fsmException){
            throw new Error("Error in creating Group FSM Transition Map", fsmException);
        }
        
        //todo - is there any more information other than the group
        // that we should pass around
        
        groupMachine = new StateMachine(groupStateMap, group);
    }
    
    // todo:  find out how a player can percieve these state changes.
    // i.e. : how should a state change effect a player?
    //      : how do we detect a change in state?
    private class CTFPlayerObjective implements PlayerObjective{
        private final Player player;
        //this state machine is for an individual agent, and only contains
        //info that this agent knows.
        private StateTransitionMap playerStateMap;
        private StateMachine playerMachine;
        
        public CTFPlayerObjective(Player player) {
            this.player = player;
            playerStateMap = new StateTransitionMap();
            StateTransitionBuilder builder = new StateTransitionBuilder(playerStateMap);
            
            State playerDone = new State(STATE_PLAYER_DONE, StateType.END);
            
            Function<State, State[][]> ourFlagTaken = ourFlagTakenFn(builder);
            Function<State, State[][]> flagTaken = flagTakenFn(builder);
            
            Function<Function<State, State[][]>, Function<State, State[][]>> 
                prefixIsBeingAttackedFn = prefixIsBeingAttacked(builder);
            
            /*
             * playerHoldingFlag = 
             *    postFix(ourFlagTaken) = beingAttacked x ourFlagTaken
             */
            State playerHoldingFlag = builder.gen(new State(STATE_PLAYER_HOLDING_FLAG, StateType.ACTIVE),
                    prefixIsBeingAttackedFn.apply(ourFlagTaken));
            
            /*
             * playerNotHoldingFlag = 
             *    isAttacking x postFix(flagTaken) = isAttacking x beingAttacked x enemyFlagTaken x ourFlagTaken 
             */
            State playerNotHoldingFlag = builder.gen(new State(STATE_PLAYER_NOT_HOLDING_FLAG, StateType.ACTIVE), parent -> {
                Function<State, State[][]> flagTakenFn = prefixIsBeingAttackedFn.apply(flagTaken);
                State isAttacking = builder.gen(new State(STATE_PLAYER_ATTACKING, StateType.ACTIVE, parent), flagTakenFn);
                State isNotAttacking = builder.gen(new State(STATE_PLAYER_NOT_ATTACKING, StateType.ACTIVE, parent), flagTakenFn);
                return new State[][]{
                    {isAttacking, isNotAttacking},
                    {isNotAttacking, isAttacking}
                };
            });
            
            //top level dfa connections
            builder.gen(null, parent -> {
               return new State[][]{
                   {playerHoldingFlag, playerDone},
                   {playerNotHoldingFlag, playerDone},
                   {playerHoldingFlag, playerNotHoldingFlag}, // in case we have a "drop flag" mechanic/action
                   {playerNotHoldingFlag, playerHoldingFlag}
               }; 
            });
            
            try {
                playerStateMap.build();
            } catch (FiniteStateException fsmException) {
                throw new Error("Error in creating Player FSM Transition Map", fsmException);
            }
            playerMachine = new StateMachine(playerStateMap, player);
        }
        
    }
    
    @Override
    public PlayerObjective makePlayerObjective(Player p) {
        return new CTFPlayerObjective(p);
    }
    
    @Override
    public ObjectiveStatus getObjectiveStatus() {
        if(group.size() == 0) return ObjectiveStatus.FAIL;
        if(groupMachine.getState().getName().equals(STATE_OUR_FLAG_CAPTURED)) return ObjectiveStatus.FAIL;
        if(groupMachine.getState().getName().equals(STATE_ENEMY_FLAG_CAPTURED)) return ObjectiveStatus.SUCCESS;
        return ObjectiveStatus.RUNNING;
    }
}

//todo - make into module
// be able to add gameobjects to a group
// this will give us a way to add in the flag