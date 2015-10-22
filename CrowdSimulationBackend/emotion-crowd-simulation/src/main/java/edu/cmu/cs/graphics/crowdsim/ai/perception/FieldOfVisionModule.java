package edu.cmu.cs.graphics.crowdsim.ai.perception;

import edu.cmu.cs.graphics.crowdsim.ai.core.GameObject;
import edu.cmu.cs.graphics.crowdsim.module.SubModule;

/**
 *  maybe : 
 *      1. update all perceptions
 *      2. update all internal states
 *      3. perform all actions.
 *              (might lead to conflicts if both agents do not expect collision, 
 *               but both move forward.)
 *  this will probably work if we just do each individually.
 *  we'll avoid collision problems naturally.
 * @author zkieda
 */
public class FieldOfVisionModule extends SubModule{
    //todo calculate based on perception CCCIP
    public double getFieldOfVision(){
        return Math.toRadians(90);
    }

    @Override
    public void onFrameUpdate() {
        //we use the game space to find out the field of vision independently
        //and with relative speed.
//        QuadTree<GameObject>.QuadTreeImmutable qti = getPlayer().getGameSpace();
        
    }
    
}

//represents a list overlapping intervals 
//this spans our sight in our field of vision module

//over each interval, we store the distance to the object, the object
//itself, and the space that the object takes up.
class OverlappingInterval<T>{
    
}
