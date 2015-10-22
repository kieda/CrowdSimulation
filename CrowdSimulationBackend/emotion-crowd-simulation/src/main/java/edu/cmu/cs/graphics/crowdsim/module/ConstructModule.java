package edu.cmu.cs.graphics.crowdsim.module;

import edu.cmu.cs.zkieda.modlang.ModuleFactory;
import edu.cmu.cs.zkieda.modlang.ModuleFactory.ModuleConstructor;
import edu.cmu.cs.zkieda.modlang.linker.NTree;
import edu.cmu.cs.zkieda.modlang.parser.ParseProgram;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

/**
 * Constructs a module from a string/file/inputstream and links using the 
 * AutoWired annotation.
 * 
 * @author zkieda
 */
public class ConstructModule {
    //1. generate ModuleFactory with given args. Add in 
    //   the verification for parameterized types
    //2. add in autowired 
    
    private static class CrowdSimModuleFactory extends ModuleFactory<SubModule>{
        private static Method getAddMethod(){
            Method m = null;
            try {
                m = MultiModule.class.getDeclaredMethod("addModule", SubModule.class);
            } catch (NoSuchMethodException | SecurityException ex) {
                reflectError("Could not find method addModule in " + MultiModule.class);
            }
            return m;
        }
        
        public CrowdSimModuleFactory() {
            super(SubModule.class, MultiModule.class, getAddMethod());
            //we do not (currently) verify the parametric types. We'll 
            //cross that bridge when we get there.
        }
    }
    private static final ModuleConstructor<SubModule> modFactory = new CrowdSimModuleFactory().build();
    public static <T extends SubModule> T build(NTree<Class<?>> tree){
        T module =  (T)modFactory.construct(tree);
        module.initInjection();
        return module;
    }
    public static <T extends SubModule> T build(InputStream modLang) throws Exception{
        return build(ParseProgram.parseAndLink(modLang));
    }
    public static <T extends SubModule> T build(File modLang) throws Exception, IOException{
        return build(new FileInputStream(modLang));
    }
    public static <T extends SubModule> T build(String modLang) throws Exception{
        return build(new ByteArrayInputStream(modLang.getBytes()));
    }
}
