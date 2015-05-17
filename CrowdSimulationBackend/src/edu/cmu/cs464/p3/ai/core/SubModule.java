package edu.cmu.cs464.p3.ai.core;

/**
 * @author zkieda
 */
public class SubModule<Parent extends MultiModule> extends Module{
    private Parent parent;
    public final void init(Parent parent){
        this.parent = parent;
    }

    public Parent getParent() {
        return parent;
    }
}
