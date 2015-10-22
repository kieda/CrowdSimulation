package edu.cmu.cs.graphics.crowdsim.serialize;

import edu.cmu.cs.graphics.crowdsim.ai.core.Flag;
import edu.cmu.cs.graphics.crowdsim.ai.core.Player;
import edu.cmu.cs.graphics.crowdsim.ai.core.NewFrameListenerModule.NewFrameListener;
import edu.cmu.cs.graphics.crowdsim.ai.core.Player.PlayerState;
import edu.cmu.cs.graphics.crowdsim.module.AutoWired;
import edu.cmu.cs.graphics.crowdsim.module.MultiModule;
import edu.cmu.cs.graphics.crowdsim.module.SubModule;
import edu.cmu.cs.graphics.crowdsim.util.Tuple;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javax.vecmath.Color3b;
import javax.vecmath.Color3f;
import javax.vecmath.Vector2d;


/**
 * 
 * @author zkieda
 */
public class SerializeGame extends MultiModule implements NewFrameListener{
	public static interface GameSerializationOut{
    	public OutputStream get() throws IOException;
    }
	
	@AutoWired private GameSerializationOut serializationSupplier;
	
	private OutputStreamWriter osr;
    private final AtomicReference<String> currentGameObjectID = new AtomicReference<String>(null);
    
    public void init() {
    	try{
    		osr = new OutputStreamWriter(serializationSupplier.get());
    	} catch(IOException e){
    		e.printStackTrace();
    	}
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

    /**
     * run at the end of every frame.
     */
	@Override
	public void run() {
		try{
            osr.write("\n(endframe)\n");
        }catch(IOException e){}
	}
}
