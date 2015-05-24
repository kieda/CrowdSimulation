package edu.cmu.cs464.p3.ai.core;

import java.util.Optional;
import java.util.OptionalInt;
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
    private final ReadOnlyObjectWrapper<OptionalInt> pickedUp;
    private final Group group;
    private final FlagState state;
    public Flag(Group group){
        position = new ComparisonObjectProperty<>();
        pickedUp = new ComparisonObjectProperty<>(OptionalInt.empty());
        this.group = group;
        this.state = new FlagState();
    }
    
    @Override
    public double getRadius() {
        return 0.125; // 0.25 diameter
    }
    
    public void pickUp(Player p){
        position.bind(p.getPositionProperty());
    }
    
    public void drop(){
        position.unbind();
    }
    
    public class FlagState{
        public ReadOnlyObjectProperty<OptionalInt> getPickedUpProperty(){
            return pickedUp.getReadOnlyProperty();
        }
        public String getTeamName(){
            return group.getGroupName();
        }
        
        public ReadOnlyObjectProperty<Vector2d> getPositionProperty() {
            return Flag.this.getPositionProperty();
        }
    }
    @Override
    public ReadOnlyObjectProperty<Vector2d> getPositionProperty() {
        return position.getReadOnlyProperty();
    }

    @Override
    public void onFrameUpdate() {}
    
    public FlagState getFlagState(){
        return state;
    }
}
