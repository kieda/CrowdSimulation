package edu.cmu.cs464.p3.ai.internal;

import org.ejml.data.D1Matrix32F;
import org.ejml.data.DenseMatrix32F;

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
    private D1Matrix32F weights;
    private D1Matrix32F uncertainties;
    private int rank;
    
    public ProbabilisticSubspace(int rank){
        this.rank = rank;
        this.weights = new DenseMatrix32F(rank, 1);
        this.uncertainties = new DenseMatrix32F(rank, 1);
    }
    
    public int getRank() {
        return rank;
    }
}
