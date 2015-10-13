package edu.cmu.cs.graphics.crowdsim.ai.core;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javax.vecmath.Vector2d;

/**
 * @author zkieda
 */
public interface Spectator extends GameObject {
    public ReadOnlyDoubleProperty directionProperty();
    public double getFieldOfVision();
    
}
