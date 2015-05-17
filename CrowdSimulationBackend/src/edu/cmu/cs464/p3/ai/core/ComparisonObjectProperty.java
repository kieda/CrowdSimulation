package edu.cmu.cs464.p3.ai.core;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @author zkieda
 */
public class ComparisonObjectProperty<T> extends ReadOnlyObjectWrapper<T> {
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
