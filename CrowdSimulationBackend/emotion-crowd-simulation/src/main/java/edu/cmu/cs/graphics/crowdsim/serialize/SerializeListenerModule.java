package edu.cmu.cs.graphics.crowdsim.serialize;

import java.util.function.Consumer;

import edu.cmu.cs.graphics.crowdsim.ai.core.GameObject;
import edu.cmu.cs.graphics.crowdsim.module.AutoWired;
import edu.cmu.cs.graphics.crowdsim.module.ListenOn;

/**
 * listens for GameObjects that we will be able to serialize during the 
 * runtime.
 *  
 * @author zkieda
 */
@ListenOn(GameObject.class)
public class SerializeListenerModule implements Consumer<GameObject>{
	
	//serialization implementation.
	@AutoWired SerializeGame gameSerialization;
	@Override
	public void accept(final GameObject t) {
		//accepts a gameobject that will be serialized. 
		//this allows us to 
		final String id = t.getObjectID();
		t.initSerialize((key, val) -> gameSerialization.put(id, key, val));
	}
}
