package edu.cmu.cs.graphics.crowdsim.ai.internal;

/**
 * This class is used when we have multiple probabilistic subspaces that are 
 * inside of this space. 
 * 
 * For example, several high level actions together form a probabilistic space
 * from the union of all their spaces. 
 * 
 * We can think of the multiprobabilisticsubspace as a map that goes from 
 * an probabilistic subspace to another probabilistic subspace. 
 * 
 * However, the mapping function is not so clear - there are several 
 * probabilistic subspace answers possible each with a different uncertainty.
 * 
 * This class is used for many different emotional operations. For example, 
 * we represent a high level action as an emotional subspace. Multiple
 * possible high level actions are combined together in a multi probabilistic
 * subspace. We find high level actions that fit our current emotions by
 * searching inside this class.
 * 
 * TODO 
 * @author zkieda
 */
public class MultiProbabilisticSubspace<V> {
    public void insert(ProbabilisticSubspace key, V val){
        
    }
    //is a vector in n dimensions 
    //carries a value V and an uncertainty for each dimension d.
    public ProbabilisticVector<V> search(ProbabilisticSubspace key){
        return null;
    }
    
    //only need to insert spaces and search for them 
    //we could make this an interface, then have a faster (sampling)
    //method, and a slower method that attempts to get full accuracy.
}
