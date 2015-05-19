package edu.cmu.cs464.p3.ai.core;

import edu.cmu.cs464.p3.ai.internal.ProbabilisticSubspace;
import java.util.Objects;

/**
 * this is merely a holder for all face types. 
 * @author zkieda
 */
public abstract class Face {
    //a face is merely a probability subspace in an n-dimensional emotion polytope
    //we define all faces here, and define a matching algorithm from 
    //our current emotional point to the face. 
    
    //here, we store the name of the face and the emotional probablity space 
    //defined by the face. 
    
    //from the space, we can gather the IMPLICATIONS of the face
    //these implications are used to decide an individual's comfort level 
    //using the face. 

    //these translations are stored in the internal package. This class is merely 
    //the front end.

    //Later, we use our internal state to develop a graph-like network to describe an emotional
    //probability space.
    //each edge on the graph has a weight and an uncertainty. each adjacent edge have a 
    //correlation factor [-1.0, 1.0]. 

    //
    
    public abstract String getName();
    public abstract ProbabilisticSubspace getProperties();
    
    @Override
    public boolean equals(Object other){
        return other instanceof Face && this.getName().equals(((Face)other).getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName());
    }
}
