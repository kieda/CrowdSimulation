package edu.cmu.cs.graphics.crowdsim.ai.perception;

import edu.cmu.cs.graphics.crowdsim.ai.internal.InternalModule;
import edu.cmu.cs.graphics.crowdsim.module.MultiModule;

/**
 * @author zkieda
 */
public class PerceptionModule extends MultiModule{
    private InternalModule internal;
    private PerceptionEmotionChange delta; 
    public void init(InternalModule internal){
        this.internal = internal;
        this.delta = new PerceptionEmotionChange();
        addModule(delta);
    }

    public InternalModule getInternal() {
        return internal;
    }

    public PerceptionEmotionChange getDelta() {
        return delta;
    }
}
