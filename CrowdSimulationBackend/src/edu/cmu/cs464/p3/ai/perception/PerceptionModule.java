package edu.cmu.cs464.p3.ai.perception;

import edu.cmu.cs464.p3.ai.core.MultiModule;
import edu.cmu.cs464.p3.ai.internal.InternalModule;

/**
 * @author zkieda
 */
public class PerceptionModule extends MultiModule{
    private InternalModule internal;
    public void init(InternalModule internal){
        this.internal = internal;
    }

    public InternalModule getInternal() {
        return internal;
    }
    
}
