package edu.cmu.cs464.p3.ai.internal;

import org.ejml.data.D1Matrix32F;
import org.ejml.data.DenseMatrix32F;
import org.ejml.simple.SimpleMatrix;

/**
 * represents a subspace in R^n where each point also has a 
 * uncertainty in each dimension. It's important to note that this 
 * space is continuous.
 * 
 * We could just represent as this as a subspace in R^{2n}
 * where the last half of the dimensions represents the uncertainty 
 * on each axis. 
 *
 * @author zkieda
 */
public class ProbabilisticSubspace {
    private SimpleMatrix weights;
    private SimpleMatrix uncertainties;
    private int rank;
    
    public ProbabilisticSubspace(int rank){
        this.rank = rank;
        this.weights = new SimpleMatrix(rank, 1);
        this.uncertainties = new SimpleMatrix(rank, 1);
    }
    
    public int getRank() {
        return rank;
    }
    
    public double getUncertainty(ProbabilisticVector v){
        
    }
}
