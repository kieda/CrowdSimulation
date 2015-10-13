package edu.cmu.cs.graphics.crowdsim.ai.action;

//represents the module that handles all actions

import edu.cmu.cs.graphics.crowdsim.ai.module.MultiModule;
import edu.cmu.cs.graphics.crowdsim.ai.core.PlayerActions;
import edu.cmu.cs.graphics.crowdsim.ai.internal.InternalModule;
import edu.cmu.cs.graphics.crowdsim.ai.module.AutoWired;
import edu.cmu.cs.graphics.crowdsim.ai.perception.PerceptionModule;


/**
 * represents a handle for the actions a player will take.
 * 
 * Utilizes the internal module and the perception module to execute various
 * tasks.
 */
public class ActionModule extends MultiModule{
    private @AutoWired InternalModule internal;
    private @AutoWired PerceptionModule perception;
    private @AutoWired PlayerActions playerActions;
    
//    public void init(InternalModule internal, PerceptionModule perception, PlayerActions playerActions){
//        this.internal = internal;
//        this.perception = perception;
//        
//        addModule(new AttackModule());
//        addModule(new FaceChangeModule());
//        addModule(new MovementModule());
//    }

    public InternalModule getInternal() {
        return internal;
    }

    public PerceptionModule getPerception() {
        return perception;
    }

    public PlayerActions getPlayerActions() {
        return playerActions;
    }
}
