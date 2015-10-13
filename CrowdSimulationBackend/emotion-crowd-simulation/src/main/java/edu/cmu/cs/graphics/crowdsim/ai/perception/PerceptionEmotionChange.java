package edu.cmu.cs.graphics.crowdsim.ai.perception;

import edu.cmu.cs.graphics.crowdsim.ai.module.MultiModule;
import edu.cmu.cs.graphics.crowdsim.ai.module.SubModule;
import org.ejml.simple.SimpleMatrix;

/**
 * @author zkieda
 */
public class PerceptionEmotionChange extends MultiModule<PerceptionModule> {
    public SimpleMatrix getMatrix(){return null;}
    public SimpleMatrix getUncertainty(){return null;}
    
    public static abstract class DeltaEmotion extends SubModule<PerceptionEmotionChange>{
        public abstract SimpleMatrix getUncertainty();
        public abstract SimpleMatrix getDelta();
        public abstract SimpleMatrix getInfluence();
    }

    @Override
    public void init() {
        
    }
    
    //todo have matrix which represents
    //todo submodules of class type
    //   DeltaEmotion
    //    + uncertainty
    //    + delta
    //    + influence
    //  will be combined to generate the getMatrix()
    //  and getUncertainty()
    //  methods.

    @Override
    public void onFrameUpdate() {
        //first, update all submodules
        super.onFrameUpdate(); 
        
        //after, use their results to get our uncertainty and matrix
//        getModulesByClass(DeltaEmotion.class).collect(null);
    }
}
