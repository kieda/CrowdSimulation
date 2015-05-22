package edu.cmu.cs464.p3.ai.core;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javax.vecmath.Vector2d;

/**
 * pick up command?
 * drop command?
 * @author zkieda
 */
public class Flag implements GameObject{
    private final ReadOnlyObjectWrapper<Vector2d> position;
    public Flag(){
        position = new ComparisonObjectProperty<>();
    }
    
    @Override
    public double getRadius() {
        return 0.125; // 0.25 diameter
    }

    @Override
    public ReadOnlyObjectProperty<Vector2d> getPositionProperty() {
        return position.getReadOnlyProperty();
    }

    @Override
    public void onFrameUpdate() {}
}
