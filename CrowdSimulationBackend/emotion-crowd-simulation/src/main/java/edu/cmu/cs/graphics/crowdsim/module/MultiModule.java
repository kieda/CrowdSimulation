package edu.cmu.cs.graphics.crowdsim.module;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Represents a MultiModule that we use for the crowd simulation project.
 * 
 * Note that we specify much of the functionality in terms of the linking 
 * unit a module is in. Typically, the linking unit is restricted to all
 * of the modules that are loaded in a single ModLang program. However,
 * in this situation, we allow manual module tree construction. All modules
 * in the same linking unit are modules that are initialized in the same tree.
 *
 * The process that modules are initialized:
 * 
 * (handled by ConstructModule)
 * 1. Building the module tree
 * 2. Linking the modules together
 * 3. Finding all listeners in the local tree
 * 4. Processing all listeners in the tree
 * 
 * (handled by calling init(null) at root)
 * 5. Initializing all modules in the tree
 * 
 * 6. Running modules in the tree.
 * 
 * All tree traversal are a depth-first search that process the root before the
 * children of the tree. We traverse the children from left to right (first to
 * last)
 * 
 * @author zkieda
 */
public class MultiModule extends SubModule {
    private List<SubModule> modules = new ArrayList<>();
    private boolean moduleInitialized = false;
    
    @Override
    public void init(MultiModule parent) {
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
        
        if(isInitialized()) {
        	//process root of child if it's not in 
        	//the same compilation unit as this one. 
        	getProcessor().process(module);
        }
    }
    
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
