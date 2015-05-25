package edu.cmu.cs464.p3.ai.core;



import com.continuent.tungsten.fsm.core.Entity;
import edu.cmu.cs464.p3.ai.internal.InternalTraits;
import java.util.ArrayList;
import java.util.List;
/**
 * organizes a series of players into a single group. Controls the creation of 
 * players, and gives players in the same group information about the body
 * 
 * In our system, a group is just an obj
 * 
 * @author zkieda
 */
public abstract class Group implements Entity{
    private final Game game; 
    private final String groupName;
    private final List<Player> players;
    
    //todo add in objective into group. 
    //add in an objective status, which gauges a certainty in failure or 
    //success. 
    
    public Group(Game g, String groupName){
        this.game = g;
        this.groupName = groupName;
        this.players = new ArrayList<>();
    }
    
    public String getGroupName(){
        return groupName;
    }
    
    public Player makePlayer(InternalTraits stats){
        Player p = new Player(this, game.newPlayerID(), stats);
        game.addGameObject(p);
        players.add(p);
        return p;
    }
    
    public int size(){
        return players.size();
    }
    
    public void removePlayer(Player p){
        players.remove(p);
    }
    
    /**
     * overridden by subclasses to determine the behavior of the group in the
     * context of the game
     */
    public abstract Objective getObjective();
}