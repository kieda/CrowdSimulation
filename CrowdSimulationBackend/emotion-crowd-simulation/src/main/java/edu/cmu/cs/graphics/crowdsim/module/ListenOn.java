package edu.cmu.cs.graphics.crowdsim.module;

/**
 * 
 * Used for a class that can listen for a specific 
 * module type to be added to the module tree. One should 
 * make a module that implements this class and place it 
 * inside of the module system. It is highly advised that
 * the user makes a fixed number of module listeners -- listeners
 * are supposed to provide a basic communication system in the framework
 * where a single module can process a dynamic number of modules as
 * they are created.
 * 
 * When we add a module to the tree that extends ModuleListener, 
 * we add the class to the list of active listeners.
 * 
 * 
 * 
 * Tells the module runtime system which module types to accept when adding a 
 * ModuleListener
 * 
 * Example code that listens to the class MyModule, and any extending classes.
 * 
 * <code>
 * @ListenOn(MyModule.class)
 * public class MyListener extends ModuleListener<MyModule>{
 *     public void accept(MyModule value){
 *         //process value here...
 *     }
 * }
 * </code>
 * @author zkieda
 * @see ModuleListener
 */
public @interface ListenOn {
	public Class<?> value();
}
