package edu.cmu.cs464.p3.ai.internal;

import org.ejml.simple.SimpleMatrix;

/**
 * @author zkieda
 */
public class ProbabilisticVector<V> {
    private V value;
    private SimpleMatrix uncertainties;
    
    public ProbabilisticVector(V value, SimpleMatrix mat) {
        this.uncertainties = mat;
        this.value = value;
    }
    
    public SimpleMatrix getUncertainties(){
        return uncertainties;
    }
    
    public V getValue() {
        return value;
    }
    
}
