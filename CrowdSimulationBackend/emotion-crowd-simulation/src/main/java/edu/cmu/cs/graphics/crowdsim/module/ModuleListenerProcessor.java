package edu.cmu.cs.graphics.crowdsim.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Consumer;


/**
 * data structure for handling all of the module listeners and processing them.
 * 
 * Version 1.0: process and find applicable modules in a naive manner by searching 
 * through the entire tree for applicable modules.
 * Version 2.0: (todo) speed improvement by quickly being able to query modules in the tree that are of a single type.
 * 
 * Semantics: listeners will only accept modules that are in its own linking unit, 
 * or the direct children of the initial linking unit.
 * 
 * @author zkieda
 */
public class ModuleListenerProcessor {
	//class listeners that we're watching for.
	private final List<Consumer<?>> listeners = new LinkedList<>(); 
	
	public void addListener(Consumer<?> val){
		ListenOn listenOn = val.getClass().getAnnotation(ListenOn.class); 
		if(listenOn != null){
			listeners.add(val);
		}
	}
	
	/**
	 * checks that the given class satisfies the generic criteria for 
	 * a modulelistener.
	 * 
	 * Requirement: 
	 * @param c
	 * @return
	 */
	public static boolean isProcessor(Class<?> c){
		return  c!= null && Consumer.class.isAssignableFrom(c) && c.getAnnotation(ListenOn.class) != null;
	}
	
	/**
	 * Processes this module through all listeners as if it were just 
	 * added.
	 */
	public void process(SubModule module){
		for(Consumer listener : getListeners(module.getClass())){
			try{
				listener.accept(module);
			} catch(ClassCastException e){
				onAnnotationMismatch(e);
			}
		}
	}
	

	/**
	 * represents the exception that was thrown when we attempted to invoke the method.
	 * 
	 * Subclasses may override this method to provide different functionality. By default, we 
	 * throw the error.
	 */
	protected void onAnnotationMismatch(ClassCastException exception) {
		throw exception;
	}
	
	/**
	 * Given a class that has been added to the module tree, we will detect if it is applicable to 
	 * any 
	 * @param moduleClass
	 * @return
	 */
	private Iterable<Consumer<?>> getListeners(final Class<?> moduleClass){
		final Iterator<Consumer<?>> it = listeners.iterator();
		return () -> new Iterator<Consumer<?>>(){
			//1. get next
			private void getNext(){
				//find currentStream if possible
				
				while(it.hasNext()){
					Consumer<?> val = it.next();
					ListenOn target = val.getClass().getAnnotation(ListenOn.class);
				
					//if the module is an instanceof the target class...
					if(target.value().isAssignableFrom(moduleClass)){
						next = val;
						return;
					}
				}
					
				hasNext = false;
			}
			
			private boolean hasNext = true;
			private Consumer<?> next;
			
			{
				//kick off iteration.
				getNext();
			}
			
			@Override
			public boolean hasNext() {
				return hasNext;
			}
			
			@Override
			public Consumer<?> next() {
				if(hasNext) {
					try{
						return next;
					} finally{
						getNext();
					}
				}
				throw new NoSuchElementException();
			}
		};
	}
}
