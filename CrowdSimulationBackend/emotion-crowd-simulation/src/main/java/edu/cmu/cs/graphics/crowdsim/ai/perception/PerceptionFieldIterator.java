package edu.cmu.cs.graphics.crowdsim.ai.perception;

import org.jgrapht.Graph;
import org.jgrapht.traverse.BreadthFirstIterator;

/**
 * Will provide a BFS through the distance-based quad-tree
 * that we created. This will provide us with the intervals 
 * of perceived sight.
 * @author zkieda
 */
public class PerceptionFieldIterator extends BreadthFirstIterator<Object, Object> {

    public PerceptionFieldIterator(Graph<Object, Object> g, Object startVertex) {
        super(g, startVertex);
    }

}
