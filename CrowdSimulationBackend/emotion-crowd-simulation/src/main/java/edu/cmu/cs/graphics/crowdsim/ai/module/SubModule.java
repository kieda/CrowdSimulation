package edu.cmu.cs.graphics.crowdsim.ai.module;

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
    
    void initInjection(){
        if(parent == null){
            new AutoModuleLinker().init(this);
        } else parent.initInjection();
    }

    public void init(Parent parent){
        this.parent = parent;
        if(getParent() == null || getParent().isInitialized()){
            init();
        }
    }

    public Parent getParent() {
        return parent;
    }
    
    public void init(){}
    public void onFrameUpdate(){}
}
