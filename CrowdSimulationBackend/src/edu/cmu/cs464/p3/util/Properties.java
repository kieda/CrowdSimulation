package edu.cmu.cs464.p3.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zkieda
 */
public class Properties {
    private Map<String, Object> properties = new HashMap<>();
    
    public Properties() {}
    
    public <T> T get(String property){
        return (T)properties.get(property);
    }

    public <T> T getOrDefault(String property, T defaultVal){
        if(properties.containsKey(property)) return get(property);
        return defaultVal;
    }

    public <T> void put(String property, T value){
        properties.put(property, value);
    }

    public boolean contains(String property){
        return properties.containsKey(property);
    }
}
