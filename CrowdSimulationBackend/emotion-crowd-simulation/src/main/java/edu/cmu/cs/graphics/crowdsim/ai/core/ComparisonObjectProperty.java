package edu.cmu.cs.graphics.crowdsim.ai.core;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @author zkieda
 */
public class ComparisonObjectProperty<T> extends ReadOnlyObjectWrapper<T> {

    public ComparisonObjectProperty(T initialValue) {
        super(initialValue);
    }

    public ComparisonObjectProperty() {}
    
    @Override
    public void set(T newValue) {
        if (isBound()) {
            throw new java.lang.RuntimeException("A bound value cannot be set.");
        }
        if (!super.get().equals(newValue)) {
            super.set(newValue);
        }
    }
}
