package edu.cmu.cs.graphics.crowdsim.ai.core;

import com.continuent.tungsten.fsm.core.Entity;
import edu.cmu.cs.graphics.crowdsim.ai.action.ActionModule;
import edu.cmu.cs.graphics.crowdsim.ai.internal.InternalModule;
import edu.cmu.cs.graphics.crowdsim.ai.internal.InternalTraits;
import edu.cmu.cs.graphics.crowdsim.ai.internal.TokenModule;
import edu.cmu.cs.graphics.crowdsim.ai.perception.FieldOfVisionModule;
import edu.cmu.cs.graphics.crowdsim.ai.perception.PerceptionModule;
import edu.cmu.cs.graphics.crowdsim.module.AutoModuleLinker;
import edu.cmu.cs.graphics.crowdsim.module.AutoWired;
import edu.cmu.cs.graphics.crowdsim.module.ConstructModule;
import edu.cmu.cs.graphics.crowdsim.module.MultiModule;

import static edu.cmu.cs.graphics.crowdsim.serialize.SerializeGame.color;
import static edu.cmu.cs.graphics.crowdsim.serialize.SerializeGame.vec2;
import edu.cmu.cs.zkieda.quadtree.QuadTree;
import java.util.Optional;
import java.util.function.BiConsumer;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javax.vecmath.Color3f;
import javax.vecmath.Vector2d;

public class Player extends MultiModule implements Spectator, Entity{
    private AutoModuleLinker injector = new AutoModuleLinker();

    public InternalTraits getTraits() {
        return traits;
    }
    
    @Override
    public void init() {
        
    }
    
    
    private int hitPoints;
    private int attackPoints;
    private final int agentID;
    private final String agentTeam;
    private final PlayerState playerState;
    private final InternalTraits traits;
    
    private @AutoWired FieldOfVisionModule fieldOfVision;
    
    private final PlayerActions playerActions;
    private PlayerObjective objective;
    
    Player(Group g, int agentID, InternalTraits traits){
        this.agentTeam = g.getGroupName();
        this.agentID = agentID;
        this.playerState = new PlayerState();
        this.traits = traits;
        
        playerState.isDead.addListener(
            (obj, oldVal, newVal) -> {
                if(newVal) g.removePlayer(Player.this);
            });
        init((MultiModule)null);
        
        this.objective = g.getObjective().makePlayerObjective(this);
        this.playerActions = new PlayerActions(this.playerState);
    }

    @Override
    public double getRadius() {
        return 0.5;
    }

    public ReadOnlyObjectProperty<Vector2d> getPositionProperty() {
        return playerState.getPositionProperty();
    }

    @Override
    public ReadOnlyDoubleProperty directionProperty() {
        return playerState.getDirectionProperty();
    }

    @Override
    public double getFieldOfVision() {
        return fieldOfVision.getFieldOfVision();
    }
    
    /**
     * Models the front-end aspects of the player. 
     * 
     * When adding a player, we also add it to the list of players serialized for 
     * serializeGame. This will add event listeners to PlayerState, which will 
     * write directly to our outputstream (synchronized). 
     * 
     * @author zkieda
     */
    public class PlayerState implements State{
        final ReadOnlyBooleanWrapper isDead;
        final ReadOnlyBooleanWrapper isAttacking;
        final ReadOnlyObjectWrapper<Color3f> moodColor;
        final ReadOnlyObjectWrapper<Vector2d> position;
        final ReadOnlyDoubleWrapper direction;
        final ReadOnlyObjectWrapper<Face> face;
        
        
        PlayerState(){
            isDead = new ReadOnlyBooleanWrapper();
            isAttacking = new ReadOnlyBooleanWrapper();
            moodColor = new ComparisonObjectProperty<>();
            position = new ComparisonObjectProperty<>();
            direction = new ReadOnlyDoubleWrapper() ;
            face = new ComparisonObjectProperty<>();
        }

        public ReadOnlyBooleanProperty isDeadProperty(){
            return isDead.getReadOnlyProperty();
        }
        public ReadOnlyBooleanProperty isAttackingProperty(){
            return isAttacking.getReadOnlyProperty();
        }
        public ReadOnlyObjectProperty<Color3f> getMoodColorProperty(){
            return moodColor.getReadOnlyProperty();
        }
        public ReadOnlyObjectProperty<Vector2d> getPositionProperty(){
            return position.getReadOnlyProperty();
        }
        public ReadOnlyObjectProperty<Face> getFaceProperty(){
            return face.getReadOnlyProperty();
        }
        public ReadOnlyDoubleProperty getDirectionProperty(){
            return direction.getReadOnlyProperty();
        }
        public String getTeamName(){
            return agentTeam;
        }
        public int getAgentID(){
            return agentID;
        }
    }
    
    public PlayerState getState(){
        return playerState;
    }

    @Override
    public String getObjectID() {
        return "" + agentID;
    }
    
    public void initSerialize(BiConsumer<String, String> fn){
        PlayerState ps = getState();
        ps.isAttackingProperty().addListener(
            (obs, oldVal, newVal) -> 
                fn.accept("attacking", newVal.toString()));
        ps.isDeadProperty().addListener(
            (obs, oldVal, newVal) -> 
                fn.accept("dead", newVal.toString()));
        ps.getMoodColorProperty().addListener(
            (obs, oldVal, newVal) -> 
                fn.accept("mood", color(newVal)));
        ps.getPositionProperty().addListener(
            (obs, oldVal, newVal) -> 
                fn.accept("position", vec2(newVal)));
        ps.getDirectionProperty().addListener(
            (obs, oldVal, newVal) -> 
                fn.accept("direction", newVal +  ""));
        ps.getFaceProperty().addListener(
            (obs, oldVal, newVal) -> 
                fn.accept("face", newVal.getName()));
        fn.accept("team", ps.getTeamName());
    }
    
    public PlayerObjective getObjective(){
        return objective;
    }
}


//  matrie