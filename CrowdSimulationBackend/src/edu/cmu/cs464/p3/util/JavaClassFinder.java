package edu.cmu.cs464.p3.util;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

//given a class, we find the instances in this data structure that 
//extend the class being searched for

//just for fun. 
interface JavaClassHolder {
    public <T> List<T> findInstances(Class<T> clazz);
}

//most efficient : we use a BST store the classes by comparison
public class JavaClassFinder implements JavaClassHolder {
    private final TreeMap<Class, List<Object>> tm = new TreeMap<Class, List<Object>>(
            (cls1, cls2) -> {
                int v = cls1.hashCode() - cls2.hashCode();
                if(v != 0) return v;
                v =  cls1.getCanonicalName().compareTo(cls2.getCanonicalName());
                if(v != 0) return v;
                return cls1.getClassLoader().toString().compareTo(cls2.getClassLoader().toString());
            }
        );
    public <T> List<T> findInstances(Class<T> clazz) {
        //1. compare by hashcodes
        //2. compare by equality
        List l = tm.get(clazz);
        return (List<T>)(l == null? new ArrayList<T>(0) : l);
    }
    private void add(Class clazz, Object o){
        List l = tm.get(clazz);
        if(l == null){
            l = new ArrayList<>();
            tm.put(clazz, l);
        }
        l.add(o);
    }
    public void addObject(Object o){
        //we just add all objects into this list (ez method
        // advanced method - store a pointer to the parent node, 
        // have a tree of the different possible classes, and just do a 
        // naive concat to find out the true list
        Arrays.stream(o.getClass().getInterfaces()).forEach(clazz -> {
            add(clazz, o);
        });
        
        Class superClasses = o.getClass();
        do{
            add(superClasses, o);
        }while((superClasses = superClasses.getSuperclass()) != null);
    }
}
