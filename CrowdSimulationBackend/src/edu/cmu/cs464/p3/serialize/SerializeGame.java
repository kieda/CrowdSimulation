package edu.cmu.cs464.p3.serialize;

import edu.cmu.cs464.p3.ai.core.Player;
import edu.cmu.cs464.p3.ai.core.Player.PlayerState;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javax.vecmath.Color3b;
import javax.vecmath.Color3f;
import javax.vecmath.Vector2d;


/**
 * 
 * @author zkieda
 */
public class SerializeGame {
    private OutputStreamWriter osr;
    private AtomicInteger currentAgentID;
    //onFrameEnd : () -> ()
    
    // add in : listener for game changes
    // evtchange = NEWPLAYER | NEXTFRAME
    // NEWPLAYER(PlayerState)
    // NEXTFRAME() 
    public SerializeGame(OutputStream os, 
        Consumer<Runnable> onNewFrame, 
        Consumer<Consumer<PlayerState>> onNewPlayer){
        osr = new OutputStreamWriter(os);
        currentAgentID = new AtomicInteger(-1);
        onNewFrame.accept(this::endFrame);
        onNewPlayer.accept(this::addPlayerState);
    }
    private synchronized void endFrame(){
        try{
            osr.write("\n(endframe)\n");
        }catch(IOException e){}
    }
    private static String vec2(Vector2d v){
        return v.x + "," + v.y;
    }
    
    private static String color(Color3f c){
        return c.x + ","+ c.y + ","+ c.z;
    }
    
    public void addPlayerState(PlayerState ps){
        ps.isAttackingProperty().addListener(
            (obs, oldVal, newVal) -> 
                put(ps.getAgentID(), "attacking", newVal.toString()));
        ps.isDeadProperty().addListener(
            (obs, oldVal, newVal) -> 
                put(ps.getAgentID(), "dead", newVal.toString()));
        ps.moodColorProperty().addListener(
            (obs, oldVal, newVal) -> 
                put(ps.getAgentID(), "mood", color(newVal)));
        ps.positionProperty().addListener(
            (obs, oldVal, newVal) -> 
                put(ps.getAgentID(), "position", vec2(newVal)));
        ps.directionProperty().addListener(
            (obs, oldVal, newVal) -> 
                put(ps.getAgentID(), "direction", vec2(newVal)));
        ps.faceProperty().addListener(
            (obs, oldVal, newVal) -> 
                put(ps.getAgentID(), "face", newVal.getName()));
        put(ps.getAgentID(), "team", ps.getTeamName());
    }
    
    private void put(final int agentID, String key, String val) {
        synchronized(SerializeGame.this){
            try{
                if(currentAgentID.compareAndSet(agentID, agentID)){
                    if(currentAgentID.get() >= 0) osr.write("\n");
                    osr.write(agentID + "=");
                }
                osr.write(key + ":" + val + ";");
            } catch(IOException e){}
        }
    }
    
    public void close() throws IOException{
        osr.flush();
        osr.close();
    }
}
