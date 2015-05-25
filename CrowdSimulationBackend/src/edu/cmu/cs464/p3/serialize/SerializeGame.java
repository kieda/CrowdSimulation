package edu.cmu.cs464.p3.serialize;

import edu.cmu.cs464.p3.ai.core.Flag;
import edu.cmu.cs464.p3.ai.core.Player;
import edu.cmu.cs464.p3.ai.core.Player.PlayerState;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
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
    private final OutputStreamWriter osr;
    private final AtomicReference currentGameObjectID;
    //onFrameEnd : () -> ()
    
    // add in : listener for game changes
    // evtchange = NEWPLAYER | NEXTFRAME
    // NEWPLAYER(PlayerState)
    // NEXTFRAME() 
    public SerializeGame(OutputStream os, 
        Consumer<Runnable> onNewFrame){
        osr = new OutputStreamWriter(os);
        currentGameObjectID = new AtomicReference(null);
        onNewFrame.accept(this::endFrame);
    }
    
    private synchronized void endFrame(){
        try{
            osr.write("\n(endframe)\n");
        }catch(IOException e){}
    }
    public static String vec2(Vector2d v){
        return v.x + "," + v.y;
    }
    
    public static String color(Color3f c){
        return c.x + ","+ c.y + ","+ c.z;
    }
    
    public void put(String id, String key, String val){
        synchronized(SerializeGame.this){
            try{
                if(currentGameObjectID.get() != null) osr.write("\n");
                if(currentGameObjectID.compareAndSet(id, id)){
                    osr.write(id + "=");
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
