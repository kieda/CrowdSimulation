package edu.cmu.cs.graphics.crowdsim.ai.module;

import edu.cmu.cs.zkieda.modlang.linker.LinkingException;
import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * links modules with the @AutoWired annotation.
 * This step actually occurs when we initialize modules. 
 * 
 * @author zkieda
 */
public class AutoModuleLinker {
    private SubModule root;
    private Map<Class<?>, SubModule> map;
    private boolean noThrow;
    public AutoModuleLinker(boolean noThrow){
        this.noThrow = noThrow;
    }

    public AutoModuleLinker() {
        this(true);
    }
    
    public void init(SubModule module){
        this.root = module;
        this.map = new HashMap<>(100);
        buildMap(root);
        link(root);
    }
    
    private void addToMap(SubModule module){
        Class<?> c = module.getClass();
        while(c != SubModule.class){
            map.put(module.getClass(), module);
            c = c.getSuperclass();
        }
        map.put(SubModule.class, module);
    }
    
    public void buildMap(SubModule module){
        if(module instanceof MultiModule){
            MultiModule mm = (MultiModule)module;
            for(SubModule child : (List<SubModule>)mm.getChildren()){
                buildMap(child);
                addToMap(child);
            }
        }
        addToMap(module);
    }
    private SubModule parseAutoWired(Iterator<Path> it, SubModule root){
        SubModule cur = root;
        //parse the autowired value.
        while(it.hasNext()){
            Path p = it.next();
            String path = p.toString();
            switch(path){
                case "..":
                    cur = cur.getParent();
                    break;
                case ".":
                    break;
                default:
                    //find child of designated type.
                    if(!(cur instanceof MultiModule)){
                        throw new AutoWiredException();
                    }
                    MultiModule mm = (MultiModule) cur;
                    List<SubModule> children = mm.getChildren();
                    List<SubModule> matches = new LinkedList<>();
                    if(p.toString().contains(".")){
                        try {
                            Class<?> c = Class.forName(path, false, getClass().getClassLoader());
                            for(SubModule sm : children){
                                if(c.equals(sm.getClass())){
                                    matches.add(sm);
                                }
                            }
                        } catch (ClassNotFoundException ex) {
                            if(!noThrow){
                                throw new AutoWiredException(ex);
                            }
                        }
                    } else{
                        //build our matches
                        for(SubModule sm : children){
                            if(sm.getClass().getSimpleName().equals(path))
                                matches.add(sm);
                        }
                    }
                    
                    if(matches.size() != 1){
                        throw new AutoWiredException();
                    }
                    cur = matches.get(0);
            }
        }
        return cur;
    }
    private void inject(Field f, SubModule obj, SubModule val){
        f.setAccessible(true);
        try {
            f.set(obj, val);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            if(!noThrow){
                throw new AutoWiredException("Could not inject ...");
            }
        }
    }
    
    private List<Field> getInheritedPrivateFields(Class<?> type) {
        List<Field> result = new ArrayList<>();

        Class<?> i = type;
        while (i != null && i != SubModule.class) {
            Field[] fs = i.getDeclaredFields();
            for(Field f : fs) result.add(f);
            i = i.getSuperclass();
        }

        return result;
    }
    public void link(SubModule module){
        if(module instanceof MultiModule){
            for(SubModule sm : (List<SubModule>)((MultiModule)module).getChildren()){
                link(sm);
            }
        }
        List<Field> fs = getInheritedPrivateFields(module.getClass());
        for(Field f : fs){
            AutoWired aw = f.getAnnotation(AutoWired.class);
            if(aw != null){
                if(!noThrow && !SubModule.class.isAssignableFrom(f.getType())){
                    throw new AutoWiredException("Specified SubModule is not a submodule");
                }
                if(aw.value().isEmpty()){
                    SubModule sm = map.get(f.getType());
                    if(sm != null){
                        inject(f, module, sm);
                    } else if(!noThrow){
                        throw new AutoWiredException("Could not find class .. ");
                    }
                } else{
                    Path p = new File(aw.value()).toPath();
                    SubModule root = p.isAbsolute() ? this.root : module;
                    SubModule inj = parseAutoWired(p.iterator(), root);
                    if(inj == null) continue;
                    inject(f, module, inj);
                }
            }
        }
    }
}
