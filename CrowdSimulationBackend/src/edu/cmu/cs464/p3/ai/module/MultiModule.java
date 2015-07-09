package edu.cmu.cs464.p3.ai.module;

import com.google.common.collect.Lists;
import edu.cmu.cs464.p3.ai.core.Player;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author zkieda
 * @param <Parent>
 */
public class MultiModule <Parent extends MultiModule> extends SubModule<Parent> {
    private List<SubModule> modules = new ArrayList<>();
//    private boolean playerInitialized = false;
    
    private boolean moduleInitialized = false;

    @Override
    public void init(Parent parent) {
        super.init(parent);
        if(getParent() == null || getParent().isInitialized()){
            moduleInitialized = true;
            modules.forEach(SubModule::init);
        }
    }
    
    boolean isInitialized() {
        return moduleInitialized;
    }
    
    public void addModule(SubModule module){
        modules.add(module);
        module.init(this);
    }

    //lazily initialize all modules added in case parent module is added 
    //afterwards
    
    //addModule : 
    //  1. add to list of children
    //  2. init player if necessary
    //  3. set the parent of the module
    //  in subModule : 
    //      1. test if parent has injector
    //      2. yes : use parent injector to build map addition and 
    //         inject autowired
    //      3. no : create new injector and initialize this as root.
    //      4. set this injector as getInjector()
    //  
    
//    @Override
//    public final void initPlayer(Player player) {
//        if(!playerInitialized){
//            super.initPlayer(player);
//            modules.forEach(m -> m.initPlayer(player));
//            player.getInjector().buildMap(this);
//            player.getInjector().link(this);
//            playerInitialized = true;
//        }
//    }
    
    @Override
    public void onFrameUpdate() {
        modules.stream().forEachOrdered(SubModule::onFrameUpdate);
    }
    
    public List<SubModule> getChildren(){
        return modules;
    }
    
    public <T extends SubModule> Stream<T> getModulesByClass(Class<T> clazz){
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
    public <T extends SubModule> Optional<T> getModuleByClass(Class<T> clazz){
        List<T> l = Lists.newArrayList(getModulesByClass(clazz).iterator());
        if(l.isEmpty()) return Optional.empty();
        if(l.size() == 1) return Optional.of(l.get(0));
        throw new Error("Expected exactly one module that falls under the class " + clazz);
    }
}
