package edu.cmu.cs.graphics.crowdsim.ai.core;

import edu.cmu.cs.graphics.crowdsim.serialize.SerializeGame;
import java.util.function.BiConsumer;
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
    public interface State{
        public ReadOnlyObjectProperty<Vector2d> getPositionProperty();
    };
    public double getRadius();
    public State getState();
    
    // information might be useless.. 
    // will calcluate wrt quadtree in a perception module.
    
//    public default ReadOnlyDoubleProperty thetaFromPointProperty(final ReadOnlyObjectProperty<Vector2d> otherPos){
//        State s = getState();
//        DoubleBinding theta = Bindings.createDoubleBinding(
//            () -> otherPos.get().angle(s.getPositionProperty().get())
//        , otherPos, s.getPositionProperty());
//        
//        ReadOnlyDoubleWrapper rodw = new ReadOnlyDoubleWrapper();
//        rodw.bind(theta);
//        return rodw.getReadOnlyProperty();
//    }
//    public default double thetaFromPoint(final Vector2d otherPos){
//        return otherPos.angle(getState().getPositionProperty().get());
//    }
//    
//    //TODO
//    public default double minThetaFromPoint(final Vector2d point){
//        //point is the viewer.
//        //what theta is the left edge of the game object from the viewer?
//        return 0;
//    }
//    public default double maxThetaFromPoint(final Vector2d point){
//        //what theta is the right edge of the game object from the viewer?
//        return 0;
//    }
    //place key, value
    public default void initSerialize(BiConsumer<String, String> serializationFn){}
    
    public String getObjectID();
}
