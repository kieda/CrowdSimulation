package edu.cmu.cs464.p3.util;

import com.continuent.tungsten.fsm.core.Action;
import com.continuent.tungsten.fsm.core.Entity;
import com.continuent.tungsten.fsm.core.Event;
import com.continuent.tungsten.fsm.core.Guard;
import com.continuent.tungsten.fsm.core.State;
import com.continuent.tungsten.fsm.core.Transition;
import com.continuent.tungsten.fsm.core.TransitionFailureException;
import com.continuent.tungsten.fsm.core.TransitionRollbackException;
import java.util.ArrayList;
import java.util.List;

/**
 * Basic aggregate transition, so we can add guards and actions after the 
 * FSM is made.
 * @author zkieda
 */
public class AggregateTransition extends Transition{
    private final AggregateGuard guards;
    private final AggregateAction actions;
    private static class AggregateGuard implements Guard{
        private List<Guard> allGuards = new ArrayList<>();
        @Override
        public boolean accept(Event message, Entity entity, State state) {
            return allGuards.stream().allMatch(g -> g.accept(message, entity, state));
        }
        public void add(Guard g){
            allGuards.add(g);
        }
    }
    private static class AggregateAction implements Action{
        private List<Action> allActions = new ArrayList<>();
        
        @Override
        public void doAction(Event message, Entity entity, Transition transition, int actionType) throws TransitionRollbackException, TransitionFailureException, InterruptedException {
            for(Action a : allActions)
                a.doAction(message, entity, transition, actionType);
        }
        public void add(Action a){
            allActions.add(a);
        }
    }
    public AggregateGuard getGuard(){
        return (AggregateGuard)super.getGuard();
    }
    public AggregateAction getAction(){
        return (AggregateAction)super.getAction();
    }
    public AggregateTransition(State input, State output) {
        super(input.getBaseName() + " to " + output.getBaseName(), new AggregateGuard(), input, new AggregateAction(), output);
        this.guards = getGuard();
        this.actions = getAction();
    }
    
    public void addGuard(Guard g){
        guards.add(g);
    }
    public void addAction(Action a){
        actions.add(a);
    }
}
