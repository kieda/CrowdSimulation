package edu.cmu.cs464.p3.ai.core;

/**
 * @author zkieda
 */
public interface ObjectiveStatus {
    public boolean hasEnded();
    public double successCertainty();
    public default double failureCertainty(){
        return 1.0 - successCertainty();
    }
}
