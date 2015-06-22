package edu.cmu.cs464.p3.modulelang;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertiesModuleFactory<T> extends ModuleFactory<T>{
	public static final String PROP_MODULE_CLASS = "moduleClass",
			PROP_MULTI_MODULE_CLASS = "multiModuleClass",
			PROP_ADD_METHOD = "addMethod";
	
	//use a default file.
	public PropertiesModuleFactory() throws IOException{
		this(new FileInputStream("./modules.properties"));
	}
	private static Properties load(InputStream file) throws IOException{
		Properties p = new Properties();
		p.load(file);
		return p;
	}
	public PropertiesModuleFactory(InputStream file) throws IOException{
		this(load(file));
	}
	private static final Pattern next = Pattern.compile("([A-Za-z_][A-Za-z_0-9]*|\\(\\)|[.])"); 
	private static interface Agg extends Function<Object, Object>{}
	private static Function<Class<?>, BiConsumer<?, ?>> parseMethod(String str){
		str = str.replaceAll("[\n\r\t \f\u000B]+", "");
        
        Matcher m = next.matcher(str);
        int prev = 0;
        String prevToken = null;
        boolean methodCall = false;
        String exceptionMsg = null;
        //initially pass the (multiModule, instance) 
        Agg aggregate = u -> u;
        exception : {
            while(m.find()){
                if(prev != m.start()){
                    exceptionMsg = "unrecognized token " + str.substring(prev, m.start());
                    break exception;
                }
                switch(m.group()){
                    case ".":
                        if(prevToken == null){
                            exceptionMsg = "expected FIELD or METHOD, found DOT";
                            break exception;
                        } 
                        if(methodCall){
                            final String methodName = prevToken;
                            final Agg fn = aggregate;
                            aggregate = u -> {
                                u = fn.apply(u);
                                try{
                                    Method method = u.getClass().getMethod(methodName);
                                    u = method.invoke(u);
                                } catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException ex){
                                    reflectError("Error accessing method " + methodName + " on object " + u, ex);
                                }
                                return u;
                            };
                        } else {
                            final String fieldName = prevToken;
                            final Agg fn = aggregate;
                            aggregate = u -> {
                                u = fn.apply(u);
                                try {
                                    Field field = u.getClass().getField(fieldName);
                                    u = field.get(u);
                                } catch (NoSuchFieldException | IllegalAccessException ex) {
                                	reflectError("Error accessing field " + fieldName + " on object " + u, ex);
                                }
                                return u;
                            };
                        }
                        methodCall = false;
                        prevToken = null;
                        break;
                    case "()":
                        if(methodCall){
                            exceptionMsg = "expected DOT, found PARENS";
                            break exception;
                        } else if(prevToken == null){
                            exceptionMsg = "expected FIELD or METHOD, found PARENS";
                            break exception;
                        }
                        methodCall = true;
                        break;
                    default : 
                        //token
                        if(prevToken != null){
                            //throw exception
                            exceptionMsg = "expected DOT, found FIELD";
                            break exception;
                        }
                        prevToken = m.group();
                        break;
                }
                prev = m.end();
            }
            if(prev != m.regionEnd()) {
                exceptionMsg = "unrecognized token " + str.substring(prev, m.regionEnd());
                break exception;
            }
            // our "add" method. This actually adds the class 
            // to our object
            
            final String methodName = prevToken;
            final Agg fn = aggregate;
            
            return cls -> new BiConsumer<Object, Object>(){
            	private Method foundMethod = null;
            	public void accept(Object u, Object v){
            		u = fn.apply(u);
            		if(foundMethod == null){
            			foundMethod = findMethod(u.getClass(), cls, methodName);
            		}
            		try{
                    	foundMethod.invoke(u, v);
                    } catch(InvocationTargetException | IllegalAccessException ex){
                    	reflectError("Error adding " + v + " to " + u + " of " + u.getClass() + " using addMethod " + methodName, ex);
                    } catch(NullPointerException ex){
                    	reflectError("Could not find addMethod " + methodName + " to " + u + " in " + u.getClass());
                    }
            	}
            };
        }
        throw new RuntimeException("error while parsing addMethod, " + exceptionMsg);
	}
	private static Method findMethod(Class<?> target, Class<?> paramType, String methodName){
		Class<?>[] interfaces = paramType.getInterfaces();
		
		while(paramType != null){
			try{
				return target.getMethod(methodName, paramType);
	        } catch(NoSuchMethodException ex){}
			paramType = paramType.getSuperclass();
		}
			
		
		for(Class<?> c: interfaces){
			try{
				return target.getMethod(methodName, c);
	        } catch(NoSuchMethodException ex){}
		}
		
		return null;
	}
	private static Class<?> load(String name){
		Class<?> val = null;
		try {
			val = Class.forName(name, false, PropertiesModuleFactory.class.getClassLoader());
		} catch (ClassNotFoundException e) {
			reflectError("Error in loading " + name + " from properties, class specified does not exist.", e);
		}
		return val;
	}
	public PropertiesModuleFactory(Properties p) {
		super(
			(Class<T>)load(p.getProperty(PROP_MODULE_CLASS)), 
			(Class<T>)load(p.getProperty(PROP_MULTI_MODULE_CLASS)),
			(BiConsumer<T, T>)(parseMethod(p.getProperty(PROP_ADD_METHOD))).apply((Class<T>)load(p.getProperty(PROP_MODULE_CLASS))));
	}
}
