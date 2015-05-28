package edu.cmu.cs464.p3.ai.core;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author zkieda
 */
public class MultiModule <Parent extends MultiModule> extends SubModule<Parent> {
    private List<SubModule> modules = new ArrayList<>();
    private boolean playerInitialized = false;
    public void addModule(SubModule module){
        modules.add(module);
        if(playerInitialized) module.init(getPlayer());
        module.init(this);
    }

    //lazily initialize all modules added in case parent module is added 
    //afterwards
    @Override
    public void init(Player player) {
        if(!playerInitialized){
            super.init(player); 
            modules.forEach(m -> m.init(player));
            playerInitialized = true;
        }
    }
    
    
    @Override
    public void onFrameUpdate() {
        modules.stream().forEachOrdered(Module::onFrameUpdate);
    }
    
    public <T extends Module> Stream<T> getModulesByClass(Class<T> clazz){
        return modules.stream().filter(clazz::isInstance).map(
                module -> (T)module);
    }
    
    /**
     * particularly useful for a parent module to aggregate a bunch of data
     * from the child modules.
     * 
     * @param <T>
     * @param clazz
     * @return 
     */
    public <T extends Module> Optional<T> getModuleByClass(Class<T> clazz){
        List<T> l = Lists.newArrayList(getModulesByClass(clazz).iterator());
        if(l.isEmpty()) return Optional.empty();
        if(l.size() == 1) return Optional.of(l.get(0));
        throw new Error("Expected exactly one module that falls under the class " + clazz);
    }
}
