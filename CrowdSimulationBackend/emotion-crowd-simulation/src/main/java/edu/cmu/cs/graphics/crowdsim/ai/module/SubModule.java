package edu.cmu.cs.graphics.crowdsim.ai.module;

import java.util.function.Consumer;

import edu.cmu.cs.graphics.crowdsim.ai.core.GameUpdatable;
import edu.cmu.cs.graphics.crowdsim.ai.core.Player;
import edu.cmu.cs.graphics.crowdsim.ai.module.MultiModule;
import edu.cmu.cs.graphics.crowdsim.ai.module.AutoModuleLinker;

/**
 * TODO make init independent of players
 * make init occur only when parent has been initialized
 * when multimodule is initialized, init all children.
 * @author zkieda
 */
public class SubModule<Parent extends MultiModule> implements GameUpdatable{
    private Parent parent;
    private ModuleListenerProcessor processor;
    
    ModuleListenerProcessor getProcessor(){
    	return processor;
    }
    
    protected void initInjection(){
        if(parent == null){
            new AutoModuleLinker().init(this);
        } else parent.initInjection();
    }

    public void init(Parent parent){
        this.parent = parent;
        if(getParent() == null || getParent().isInitialized()){
        	processor = new ModuleListenerProcessor();
            init();
        } else processor = getParent().getProcessor();
        
        if(ModuleListenerProcessor.isProcessor(this.getClass())){
        	//add the listener to the processor. 
        	processor.addListener((Consumer<?>)this);
        }
        
        if(getParent() == null || getParent().isInitialized()){
        	//if this condition is satisfied, we are initializing 
        	//this Module for the first time. 
        	//We know that this linking unit will not grow any more,
        	//so all of the listeners have been added to the processor
        	//possible.
        	
        	//we test if this submodule is in our local processor.
        	
        	processor.process(this);
        }
    }
    

    public Parent getParent() {
        return parent;
    }
    
    public void init(){}
    public void onFrameUpdate(){}
}
