package edu.cmu.cs.graphics.crowdsim.ai.core;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import edu.cmu.cs.graphics.crowdsim.module.ListenOn;
import edu.cmu.cs.graphics.crowdsim.module.MultiModule;
import edu.cmu.cs.graphics.crowdsim.module.SubModule;

/**
 * We only accept NewFrameListener to not confuse any other modules that may 
 * implement the Runnable method. However, we accept any Runnable as a new frame
 * listener.
 * 
 * @author zkieda
 */
@ListenOn(NewFrameListenerModule.NewFrameListener.class)
public class NewFrameListenerModule extends SubModule implements Consumer<Runnable>{
	public static interface NewFrameListener extends Runnable{}
	
	private List<Runnable> onNewFrame = new ArrayList<>();
	@Override public void accept(Runnable t) {
		onNewFrame.add(t);
	}
	
	void onNewFrame(){
		onNewFrame.forEach(Runnable::run);
	}
}
