package edu.cmu.cs.graphics.crowdsim.module;

import java.util.function.Consumer;

import edu.cmu.cs.graphics.crowdsim.ai.core.GameUpdatable;
import edu.cmu.cs.graphics.crowdsim.ai.core.Player;
import edu.cmu.cs.graphics.crowdsim.module.AutoModuleLinker;
import edu.cmu.cs.graphics.crowdsim.module.MultiModule;

/**
 * 
 * @author zkieda
 */
public class SubModule implements GameUpdatable{
    private MultiModule parent;
    private ModuleListenerProcessor processor;
    
    ModuleListenerProcessor getProcessor(){
    	return processor;
    }
    
    private void addListeners(){
    	if(parent == null) processor = new ModuleListenerProcessor();
    	else processor = parent.getProcessor();
    	
        if(ModuleListenerProcessor.isProcessor(this.getClass()))
    		processor.addListener((Consumer<?>)this);
        
        if(this instanceof MultiModule) 
        	for(SubModule module : ((MultiModule)this).getChildren()) module.addListeners();
    }
    
    private void injectListeners(){
    	processor.process(this);
    	
    	if(this instanceof MultiModule)
    		for(SubModule module : ((MultiModule)this).getChildren()) module.injectListeners();
    }
    
    protected void initInjection(){
        if(parent == null){
        	//this block will be called once at the root of the tree.
        	
        	//1. inject AutoWired 
            new AutoModuleLinker().init(this);
            
           
        } else {
        	parent.initInjection();
        }
    }

    public void init(MultiModule parent){
        this.parent = parent;
        if(getParent() == null || getParent().isInitialized()){
            init();
        }
        if(getParent() == null){
        	 //2. find all listeners for the processor
            addListeners();
            
            //3. process all submodules applicable to the processor
            injectListeners();
        }
    }
    

    public MultiModule getParent() {
        return parent;
    }
    
    public void init(){}
    public void onFrameUpdate(){}
}
