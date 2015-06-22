package edu.cmu.cs464.p3.modulelang;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import scala.collection.Iterator;
import edu.cmu.cs464.p3.modulelang.linker.NTree;

public abstract class ModuleFactory<T> {
	private List<Consumer<NTree<Class<?>>>> verification = new ArrayList<>(3);
	private final Class<T> moduleClass;
	private final Class<? extends T> multiModuleClass;
	private final BiConsumer<T, T> addMethod;
	
	protected static void reflectError(String details, Throwable cause){
		throw new ModLangLinkingError("Error in generating tree reflectively :\n" + details, cause);
	}
	protected static void reflectError(String details){
		throw new ModLangLinkingError("Error in generating tree reflectively :\n" + details);
	}
	
	/**
	 * note that we must have {@code multiModuleClass} <: {@code moduleClass}, which means that 
	 * the moduleClass is a supertype of multiModuleClass.
	 * @param moduleClass
	 * @param multiModuleClass
	 * @param addMethod(multiModule, childModule) will add the childModule to the superModule  
	 */
	public ModuleFactory(Class<T> moduleClass, Class<? extends T> multiModuleClass, BiConsumer<T, T> addMethod){
		this.moduleClass = moduleClass;
		this.multiModuleClass = multiModuleClass;
		this.addMethod = addMethod;
		
		//verifies that the classes are the correct type in our tree.
		Function<NTree<Class<?>>, Class<?>> verifyFn = NTree.traverseUp((L, l) -> {
			if(!(L.isEmpty() || multiModuleClass.isAssignableFrom(l))){
				reflectError("Parent " + l + " is not an instance of specified multi module class " + this.multiModuleClass);
			}
			return l;
		},
		(Class<?> l) -> {
			if(!moduleClass.isAssignableFrom(l)){
				reflectError("Child " + l + " is not an instance of specified module class " + moduleClass);
			}
			return l;
		});
		
		if(!moduleClass.isAssignableFrom(multiModuleClass)){
			reflectError("MultiModule class " + multiModuleClass + " does not extend module class " + moduleClass);
		}
		//add this verification
		addVerification(verifyFn::apply);
	}
	
	public ModuleFactory(Class<T> moduleClass, Class<? extends T> multiModuleClass, Method addMethod) {
		this(moduleClass, multiModuleClass, (parent, child) -> {
			try {
				addMethod.invoke(parent, child);
			} catch (InvocationTargetException | IllegalAccessException e) {
				reflectError("In adding child " + child + " to parent " + parent + " using method " + addMethod, e);
			}
		});
		//check that the addMethod is actually in our multi module class
		//this is true if the method is a superclass of our multi module class. 
		if(!addMethod.getDeclaringClass().isAssignableFrom(multiModuleClass)){
			reflectError("The add method " + addMethod + " does not belong to the specified multiModuleClass " + multiModuleClass);
		}
	}
	
	public final ModuleFactory<T> addVerification(Consumer<NTree<Class<?>>> verifyFn){
		verification.add(verifyFn);
		return this;
	}
	
	public static class ModuleConstructor<S>{
		private final List<Consumer<NTree<Class<?>>>> verification;
		private final Function<NTree<Class<?>>, S> moduleGeneration;
		
		@SuppressWarnings("unchecked")
		private ModuleConstructor(List<Consumer<NTree<Class<?>>>> verification,
				BiConsumer<S, S> addMethod,
				Class<S> moduleClass){
			this.verification = new ArrayList<>(verification);
			moduleGeneration = NTree.traverseUp((L, l) -> {
				//parent object -- instance of multi module.
				S multiModule = l;
				
				//children instance of S, child module.
	            Iterator<S> it = L.iterator();
	            
	            while(it.hasNext()){
	                S m = it.next();
	                addMethod.accept(multiModule, m);
	            }
	            
	            return multiModule;
			}, (Class<?> l) -> {
				S re = null;
				try {
					re = (S)l.newInstance();
				} catch (IllegalAccessException | InstantiationException e) {
					reflectError("Exception in creating class " + l, e);
				} catch (ClassCastException v){
					reflectError("Class " + l + " is not an instance of the module class " + moduleClass);
				}
				return re;
			});
		}
		public S construct(NTree<Class<?>> moduleTree){
			//1. perform verification
			verification.forEach(v -> v.accept(moduleTree));
			
			//2. perform tree construction.
			//   if the client did proper verification, we 
			//   should have no problems.
			return moduleGeneration.apply(moduleTree);
		}
	}
	
	public ModuleConstructor<T> build(){
		return new ModuleConstructor<>(verification, addMethod, moduleClass);
	}
}
