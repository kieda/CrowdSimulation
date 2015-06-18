package edu.cmu.cs464.p3.ai.module;

import edu.cmu.cs464.p3.ai.core.Module;
import edu.cmu.cs464.p3.ai.core.MultiModule;
import edu.cmu.cs464.p3.ai.core.SubModule;
import edu.cmu.cs464.p3.modulelang.ParseProgram;
import edu.cmu.cs464.p3.modulelang.linker.LinkingException;
import edu.cmu.cs464.p3.modulelang.linker.NTree;
import java.io.InputStream;
import java.util.function.Function;
import scala.collection.Iterator;
import scala.collection.Traversable;

/**
 * @author zkieda
 */
public class ConstructModule {
    public static <T extends Module> T gen(InputStream in){
        NTree<Class<?>> tree;
        try {
             tree = ParseProgram.parseAndLink(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Function<NTree<Class<?>>, Module> f = NTree.<Module, Class<?>>traverseUp((L, l) -> {
            MultiModule<?> mm = (MultiModule<?>)l;
            Iterator<Module> it = L.iterator();
            while(it.hasNext()){
                SubModule<?> m = (SubModule<?>)it.next();
                mm.addModule(m);
            }
            return mm;
        }, k -> {
                try {
                    return (Module)k.newInstance();
                } catch (InstantiationException | IllegalAccessException ex) {
                    throw new LinkingException("Class " + k + " could not be instantiated");
                }
            }
        );
        return (T)f.apply(tree);
        

    }
}
