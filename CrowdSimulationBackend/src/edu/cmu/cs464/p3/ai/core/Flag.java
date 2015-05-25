package edu.cmu.cs464.p3.ai.core;

import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import edu.cmu.cs464.p3.serialize.SerializeGame;
import edu.cmu.cs464.p3.util.Tuple;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.BiConsumer;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javax.vecmath.Vector2d;

/**
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
    public void init(Vector2d initialPos){
        position.set(initialPos);
    }
    //TODO : have a way for the game to add in an arbitrary gameObject
    //      to a game/group
    //      
    //      for flags : 
    //          1. need group
    //          2. need to add it to the serialize game.
    //      maybe : 
    //      GameObject :: init( ... )
    //      Game :: addGameObject(GameObject go)
    
    @Override
    public double getRadius() {
        return 0.125; // 0.25 diameter
    }
    
    public void pickUp(Player p){
        if(!pickedUp.get().isPresent()){
            pickedUp.set(OptionalInt.of(p.getState().getAgentID()));
            position.bind(p.getPositionProperty());
        }
    }
    
    public void drop(Vector2d pos){
        pickedUp.get().ifPresent(i -> {
            position.unbind();
            position.set(pos);
            pickedUp.set(OptionalInt.empty());
        });
    }
    
    public class FlagState implements State{
        public ReadOnlyObjectProperty<OptionalInt> getPickedUpProperty(){
            return pickedUp.getReadOnlyProperty();
        }
        public String getTeamName(){
            return group.getGroupName();
        }
        
        public ReadOnlyObjectProperty<Vector2d> getPositionProperty() {
            return position.getReadOnlyProperty();
        }
    }
    
    @Override
    public void onFrameUpdate() {}
    
    public FlagState getState(){
        return state;
    }

    @Override
    public String getObjectID() {
        return "flag-" + group.getGroupName();
    }

    @Override
    public void initSerialize(BiConsumer<String, String> fn) {
        state.getPositionProperty().addListener(
            (obj, oldVal, newVal) -> {
                if(!state.getPickedUpProperty().get().isPresent())
                    fn.accept("position", SerializeGame.vec2(newVal));
            });
        state.getPickedUpProperty().addListener(
            (obj, oldVal, newVal) -> {
                if(newVal.isPresent()) fn.accept("pickedUp", "" + newVal.getAsInt());
                else fn.accept("drop", SerializeGame.vec2(state.getPositionProperty().get()));
            });
    }
//    public Map<ReadOnlyProperty, Tuple.Tuple2<String, String>> serialize(){
//        return Maps.asMap(
//            Sets.immutableEnumSet(position, otherElements), null)
//    }
}
