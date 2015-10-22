package edu.cmu.cs.graphics.crowdsim;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import edu.cmu.cs.graphics.crowdsim.module.AutoWired;

public class Test {
	@AutoWired
	private Collection<String> collection = null;
	
	public static void main(String[] args) throws Exception{
		Field[] fields = Test.class.getDeclaredFields();
		
		for(Field f : fields){
			if(f.isAnnotationPresent(AutoWired.class)){
				System.out.println(((ParameterizedType)f.getGenericType()).getActualTypeArguments()[0]);
			}
		}
	}
	
}
