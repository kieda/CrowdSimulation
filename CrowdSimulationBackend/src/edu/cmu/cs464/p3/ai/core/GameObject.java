package edu.cmu.cs464.p3.ai.core;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javax.vecmath.Vector2d;

/**
 * represents a game object on the field. 
 * GameObjects are canonically circular 
 * @author zkieda
 */
public interface GameObject extends GameUpdatable{
    public double getRadius();
    public ReadOnlyObjectProperty<Vector2d> getPositionProperty();
    public default ReadOnlyDoubleProperty thetaFromPointProperty(final ReadOnlyObjectProperty<Vector2d> otherPos){
        DoubleBinding theta = Bindings.createDoubleBinding(
            () -> otherPos.get().angle(getPositionProperty().get())
        , otherPos, getPositionProperty());
        
        ReadOnlyDoubleWrapper rodw = new ReadOnlyDoubleWrapper();
        rodw.bind(theta);
        return rodw.getReadOnlyProperty();
    }
    public default double thetaFromPoint(final Vector2d otherPos){
        return otherPos.angle(getPositionProperty().get());
    }
    
    //TODO
    public default double minThetaFromPoint(final Vector2d point){
        //point is the viewer.
        //what theta is the left edge of the game object from the viewer?
        return 0;
    }
    public default double maxThetaFromPoint(final Vector2d point){
        //what theta is the right edge of the game object from the viewer?
        return 0;
    }
}
