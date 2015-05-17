package edu.cmu.cs464.p3.ai.core;

import edu.cmu.cs464.p3.util.Interval;
import javafx.beans.property.ReadOnlyObjectProperty;
import javax.vecmath.Vector2d;

/**
 * @author zkieda
 */
public interface Spectator extends GameObject {
    public ReadOnlyObjectProperty<Vector2d> directionProperty();
    public double getFieldOfVision();
    
    //returns the interval that the other object falls into this object's 
    //field of view
    public default Interval inFieldOfView(GameObject other){
        return null;
    }
}
