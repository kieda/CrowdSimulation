package edu.cmu.cs464.p3.ai.core;

import com.continuent.tungsten.fsm.core.Entity;
import edu.cmu.cs464.p3.ai.action.ActionModule;
import edu.cmu.cs464.p3.ai.internal.InternalModule;
import edu.cmu.cs464.p3.ai.internal.InternalTraits;
import edu.cmu.cs464.p3.ai.internal.TokenModule;
import edu.cmu.cs464.p3.ai.perception.PerceptionModule;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javax.vecmath.Color3f;
import javax.vecmath.Vector2d;

public class Player extends MultiModule implements GameObject, Entity{
    private int hitPoints;
    private int attackPoints;
    private final int agentID;
    private final String agentTeam;
    private final PlayerState playerState;
    private final InternalTraits traits;
    
    private final TokenModule tokens;
    private final ActionModule action;
    private final PerceptionModule perception;
    private final InternalModule internal;
    
    private final PlayerActions playerActions;
    
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
        init((Player)this);
        
        tokens = new TokenModule();
        internal = new InternalModule();        
        action = new ActionModule();
        perception = new PerceptionModule();
        
        addModule(tokens);
        addModule(perception);
        addModule(internal);
        addModule(action);
        
        this.playerActions = new PlayerActions(this.playerState);
        
        action.init(internal, perception, playerActions);
        internal.init(perception, playerActions::setMoodColor);
        perception.init(internal);
    }

    @Override
    public double getRadius() {
        return 0.5;
    }

    @Override
    public ReadOnlyObjectProperty<Vector2d> getPositionProperty() {
        return playerState.positionProperty();
    }

    public InternalTraits getTraits() {
        return traits;
    }

    public TokenModule getTokenModule() {
        return tokens;
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
    public class PlayerState {
        final ReadOnlyBooleanWrapper isDead;
        final ReadOnlyBooleanWrapper isAttacking;
        final ReadOnlyObjectWrapper<Color3f> moodColor;
        final ReadOnlyObjectWrapper<Vector2d> position;
        final ReadOnlyObjectWrapper<Vector2d> direction;
        final ReadOnlyObjectWrapper<Face> face;

        PlayerState(){
            isDead = new ReadOnlyBooleanWrapper();
            isAttacking = new ReadOnlyBooleanWrapper();
            moodColor = new ComparisonObjectProperty<>();
            position = new ComparisonObjectProperty<>();
            direction = new ComparisonObjectProperty<>();
            face = new ComparisonObjectProperty<>();
        }

        public ReadOnlyBooleanProperty isDeadProperty(){
            return isDead.getReadOnlyProperty();
        }
        public ReadOnlyBooleanProperty isAttackingProperty(){
            return isAttacking.getReadOnlyProperty();
        }
        public ReadOnlyObjectProperty<Color3f> moodColorProperty(){
            return moodColor.getReadOnlyProperty();
        }
        public ReadOnlyObjectProperty<Vector2d> positionProperty(){
            return position.getReadOnlyProperty();
        }
        public ReadOnlyObjectProperty<Face> faceProperty(){
            return face.getReadOnlyProperty();
        }
        public ReadOnlyObjectProperty<Vector2d> directionProperty(){
            return direction.getReadOnlyProperty();
        }
        public String getTeamName(){
            return agentTeam;
        }
        public int getAgentID(){
            return agentID;
        }
    }
    
    public PlayerState getPlayerState(){
        return playerState;
    }
}


//  matrie