package edu.cmu.cs.graphics.crowdsim.ai.internal;

import org.ejml.simple.SimpleMatrix;

/**
 * The internal traits for a player, according to the CCCIP model
 * @author zkieda
 */
public class InternalTraits {
    
    private final double 
        communication,
        confidence, 
        courage, 
        intelligence,
        perception;
        
    //each range from -1.0 to 1.0
    public InternalTraits(
        double communication,
        double confidence, 
        double courage, 
        double intelligence, 
        double perception){
        this.communication = communication;
        this.confidence = confidence;
        this.courage = courage;
        this.intelligence = intelligence;
        this.perception = perception;
    }

    public double getCommunication() {
        return communication;
    }

    public double getConfidence() {
        return confidence;
    }

    public double getCourage() {
        return courage;
    }

    public double getIntelligence() {
        return intelligence;
    }

    public double getPerception() {
        return perception;
    }
    public SimpleMatrix getVector(){
        return new SimpleMatrix(new double[][]{{
            getCommunication(), 
            getConfidence(),
            getCourage(),
            getIntelligence(),
            getPerception()
        }}).transpose();
    }
}
