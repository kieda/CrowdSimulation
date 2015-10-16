package edu.cmu.cs.graphics.crowdsim;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;

import edu.cmu.cs.graphics.crowdsim.ai.module.SubModule;

public class Test {
	/**
	 * @param args
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		
		
	}
}
class D extends SubModule{
	public void pt(){
		System.out.println("wow!");
	}
}