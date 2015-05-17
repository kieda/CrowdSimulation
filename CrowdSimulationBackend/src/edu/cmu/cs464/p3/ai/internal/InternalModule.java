package edu.cmu.cs464.p3.ai.internal;

import edu.cmu.cs464.p3.ai.core.MultiModule;
import edu.cmu.cs464.p3.ai.perception.PerceptionModule;

//represents a model that affects the internal behavior.
public class InternalModule extends MultiModule{
    private EmotionLinearSystem internalState;
    private PerceptionModule perception;
    
    public EmotionLinearSystem getInternalState() {
        return internalState;
    }

    public PerceptionModule getPerception() {
        return perception;
    }
    
    public void init(PerceptionModule perception){
        this.perception = perception;
    }
}