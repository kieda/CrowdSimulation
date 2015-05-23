package edu.cmu.cs464.p3.ai.internal;

import edu.cmu.cs464.p3.util.MatrixUtil;
import java.util.Random;
import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.random.GaussianRandomGenerator;
import org.apache.commons.math3.random.Well1024a;
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
    
    public ProbabilisticSubspace(int rank, SimpleMatrix weights, SimpleMatrix uncertainties){
        MatrixUtil.checkDims(weights, rank, 1);
        MatrixUtil.checkDims(uncertainties, rank, 1);
        this.rank = rank;
        this.weights = weights;
        this.uncertainties = uncertainties;
    }
    
    public ProbabilisticSubspace(int rank){
        this.rank = rank;
        this.weights = new SimpleMatrix(rank, 1);
        this.uncertainties = new SimpleMatrix(rank, 1);
    }
    
    public ProbabilisticSubspace transform(SimpleMatrix M){
        //must have "rank" cols
        //new rank will be M.numRows
        MatrixUtil.checkNumCol(M, rank);
        return new ProbabilisticSubspace(M.numRows(), M.mult(weights), M.mult(uncertainties));
    }
    
    public SimpleMatrix probabilityDiff(SimpleMatrix vec){
        MatrixUtil.checkDims(vec, rank, 1);
        final SimpleMatrix diff = vec.minus(weights);
        
        return MatrixUtil.fill(new SimpleMatrix(rank, 1),    
            (row, col) -> 
                new Gaussian(0, uncertainties.get(row, col)).value(diff.get(row, col)));
        
    }
    
    //TODO
    public ProbabilisticSubspace merge(ProbabilisticSubspace other){
        return null;
    }
    
    public ProbabilisticSubspace probabilityDiff(ProbabilisticSubspace other){
        return null;
    }
    
    /**
     * gives the length of this space, also accounting for uncertainties
     * TODO
     * @return 
     */
    public double length(){return 0;}
    
    public int getRank() {
        return rank;
    }

    public SimpleMatrix getWeights() {
        return weights;
    }

    public SimpleMatrix getUncertainties() {
        return uncertainties;
    }
    
    private Random rand = new Random();
    /**
     * @return a random point in our space, with probabilities expected from 
     * our probability space
     */
    public SimpleMatrix getRandomValue(){
        return weights.plus(MatrixUtil.tabulate(rank, 1, 
                (j, i) -> uncertainties.get(j, i) * rand.nextDouble()));
    }
}
