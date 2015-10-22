package edu.cmu.cs.graphics.crowdsim.ai.action;

import edu.cmu.cs.graphics.crowdsim.ai.core.PlayerActions;
import edu.cmu.cs.graphics.crowdsim.ai.internal.InternalModule;
import edu.cmu.cs.graphics.crowdsim.ai.perception.PerceptionModule;
import edu.cmu.cs.graphics.crowdsim.module.AutoWired;
import edu.cmu.cs.graphics.crowdsim.module.MultiModule;


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
