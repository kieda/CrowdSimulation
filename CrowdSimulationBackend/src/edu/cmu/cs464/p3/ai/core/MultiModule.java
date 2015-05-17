package edu.cmu.cs464.p3.ai.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author zkieda
 */
public class MultiModule <Parent extends MultiModule> extends SubModule<Parent> {
    private List<SubModule> modules = new ArrayList<>();
    public void addModule(SubModule module){
        modules.add(module);
        module.init(getPlayer());
        module.init(this);
    }

    @Override
    public void onFrameUpdate() {
        modules.stream().forEachOrdered(Module::onFrameUpdate);
    }
    
    public <T extends Module> Iterator<T> getModulesByClass(Class<T> clazz){
        return modules.stream().filter(clazz::isInstance).map(
                module -> (T)module
        ).iterator();
    }
}
