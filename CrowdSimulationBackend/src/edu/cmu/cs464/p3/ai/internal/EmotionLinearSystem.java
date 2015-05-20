package edu.cmu.cs464.p3.ai.internal;

import static edu.cmu.cs464.p3.util.MatrixUtil.fill;
import static edu.cmu.cs464.p3.ai.internal.InternalModule.NUM_MOODS;
import edu.cmu.cs464.p3.ai.core.SubModule;
import edu.cmu.cs464.p3.util.LinearProgram;
import edu.cmu.cs464.p3.util.MatrixUtil;
import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleGraph;
import org.ejml.simple.SimpleMatrix;
import edu.cmu.cs464.p3.util.Tuple;
import java.util.Optional;
/**
 * this is the linear system built from the graph [see section 5].
 * Every agent has this linear system. We update this system every timestep, 
 * along with the uncertainties in the updates.
 * 
 * @author zkieda
 */
public class EmotionLinearSystem extends SubModule<InternalModule> {
    
    /* TODO
     * main idea : 
     * 
     * The final emotional value is the summation of all including vertices, 
     * including a self-loop. We weight the self-loop more than most, and 
     * the rest have the corresponding correlation factors to this vertex.
     * 
     * Emotion_j = (sum_{(j, i) in E} influence(i, j) * weight_i) + influence(j, j) * weight_j 
     */
    
    /* FINAL TODO:  
     * find a way to dampen the uncertainty in emotions. 
     */
    
    static class MoodVertex {
        private final int id;
        
        //fixed. Determines summation value at the vertex
        private final double c;

        public MoodVertex(int id, double c) {
            this.id = id;
            this.c = c;
        }
    }
    
    static class MoodEdge {
        //multiplied by our linear constraint.
        // + alpha is a negative correlation
        // - alpha is a positive correlation
        private final double lambda;
        
        //added to our linear constraint. fixed
        private final double alpha;

        public MoodEdge(double lambda, double alpha) {
            this.lambda = lambda;
            this.alpha = alpha;
        }
    }
    
    private MoodVertex[] vertices;
    private Graph<MoodVertex, MoodEdge> moodGraph;
    
    //represents the current space of moods
    private ProbabilisticSubspace moodSpace;
    
    //represents our linear program
    private LinearProgram emotionProgram;
    
    private void addEdges(int fromVertex, int... toVerts){
        for(int i : toVerts) 
            moodGraph.addEdge(vertices[fromVertex], vertices[i]);
    }
    
    private void initMoodGraph(){
        //todo - find out the correct initial values given
        //the input CCCIP model
//        getPlayer().getTraits()
        
        moodGraph = new SimpleGraph<>(
                (v1, v2) -> new MoodEdge(0, 0)
        );
        vertices = new MoodVertex[NUM_MOODS];
        for(int i = 0; i < NUM_MOODS; i++) {
            MoodVertex mv = new MoodVertex(i, 0);
            vertices[i] = mv;
            moodGraph.addVertex(mv);
        }
        
        addEdges(0, 1, 7, 4, 6);
        addEdges(1, 2, 4, 5);
        addEdges(2, 3, 5, 6);
        addEdges(3, 7);
        addEdges(5, 6);
    }
    
    private int numVerts(){
        return moodGraph.vertexSet().size();
    }
    private int numEdges(){
        return moodGraph.edgeSet().size();
    }
    
    private double lambda(int j, int i){
        MoodEdge me = moodGraph.getEdge(vertices[j], vertices[i]);
        return me == null ? 0.0 : me.lambda;
    }
    private double alpha(int j, int i){
        MoodEdge me = moodGraph.getEdge(vertices[j], vertices[i]);
        return me == null ? 0.0 : me.alpha;
    }
    
    @Override
    public void init() {
        //make mood graph
        initMoodGraph();
        
        //make mood matrix
        moodSpace = new ProbabilisticSubspace(NUM_MOODS);
        
        initMoodMatrix();
    }

    private void initMoodMatrix() {
        // then the linear relation ship is
        //    a + \lambda v
        // have matrix
        
        // j : sum_i {a_i + \lambda_i v_i}
        emotionProgram = new LinearProgram();
        
        SimpleMatrix lambdas = new SimpleMatrix(numEdges(), numVerts());
        SimpleMatrix consts = new SimpleMatrix(numVerts(), 1);
        SimpleMatrix ones = new SimpleMatrix(numVerts(), 1);
        SimpleMatrix alphas = new SimpleMatrix(numEdges(), numVerts());
        
        fill(alphas, this::alpha);
        fill(lambdas, this::lambda);
        fill(consts, (j, i) -> vertices[j].c);
        fill(ones, (j, i) -> 1.0);
        
        //\lambda dot v represents the left hand side
        // have LHS <= RHS
        emotionProgram.addLEMatrixConstraints(lambdas, alphas.mult(ones).minus(consts));
    }
    
    private SimpleMatrix getDelta(){ 
        return getParent().getPerception().getDelta().getMatrix();
    }
    
    private SimpleMatrix getEmotion(){
        return moodSpace.getWeights();
    }

    public ProbabilisticSubspace getMoodSpace() {
        return moodSpace;
    }
    
    @Override
    public void onFrameUpdate() {
        final SimpleMatrix delta = getDelta();
        final SimpleMatrix emotion = getEmotion();
        
        Optional<Tuple.Tuple2<LinearProgram.Constraint, Double>> d = MatrixUtil
            .toStream(emotionProgram.getConstraints())
            .filter(c -> c.willIntersect(delta))
            .map(c -> new Tuple.Tuple2<>(c, c.intersectionDistance(delta, emotion)))
            .filter(t -> !t.get1().satisfy(t.get2()))
            .min((c1, c2) -> c1.get2().compareTo(c2.get2()));
        
        final SimpleMatrix newEmotion;
        if(d.isPresent()) {
            newEmotion = getEmotion().mult(delta.scale(d.get().get2()));
        } else{
            newEmotion = getEmotion();
        }
        
    }
}
