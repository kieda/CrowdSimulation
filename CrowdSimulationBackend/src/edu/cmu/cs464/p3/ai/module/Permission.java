package edu.cmu.cs464.p3.ai.module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * We take the intersection of the permissions requested by the 
 * value() and the permission(). 
 * 
 * It's important to note that modules that have this annotation will 
 * affect the way the AutoModuleLinker works. If we specify that 
 * a module is only accessable to the children, then this module will not
 * show up in an autoinject to any of the children of this module.
 * 
 * TODO : 
 * When this annotation is repeated, we union together the set of classes that
 * are acceptable for each annotation.
 * @author zkieda
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
//@Repeatable(Permissions.class)
public @interface Permission {
    /**
     * specifies specific classes that are whitelisted for accessing
     * this class. The special case is the empty array, which means that
     * all classes are allowed.
     */
    public Class<?>[] value() default {};
    
    /**
     * specifies the permission type
     */
    public static enum PermissionPolicy{
        CHILDREN,
        ALL,
        PARENT
    }
    /**
     * specifies the permission policy that will be used when linking this 
     * module. The table is seen below.
     * 
     * CHILDREN  :  only allow children of the current module, and the module itself
     * ALL       :  allow all modules to access this one
     * PARENT    :  allow parent modules of this one to access this module.
     * 
     * We union together the set of classes that are given permission to 
     * access this class. 
     * 
     * For example, {CHILDREN, PARENT} represents that ancestors and 
     * descendants can access this module, but not modules on a different tree
     * 
     * For example, {PARENT, ALL} = {CHILDREN, ALL} = ALL
     */
    public PermissionPolicy[] permission() default PermissionPolicy.ALL;
}
