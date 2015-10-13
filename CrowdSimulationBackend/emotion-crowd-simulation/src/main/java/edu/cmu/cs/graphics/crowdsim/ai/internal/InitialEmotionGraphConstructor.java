package edu.cmu.cs.graphics.crowdsim.ai.internal;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;
import edu.cmu.cs.graphics.crowdsim.ai.core.Player;
import edu.cmu.cs.graphics.crowdsim.ai.module.SubModule;
import org.jgrapht.Graph;
import static edu.cmu.cs.graphics.crowdsim.ai.internal.EmotionLinearSystem.*;
import edu.cmu.cs.graphics.crowdsim.ai.module.AutoWired;

/**
 * @author zkieda
 */
public abstract class InitialEmotionGraphConstructor extends SubModule<EmotionLinearSystem>{
    public abstract Graph<MoodVertex, MoodEdge> 
        buildGraph();
}
class BasicEmotionGraphConstructor extends InitialEmotionGraphConstructor{
    private @AutoWired Player player;
    @Override
    public Graph<EmotionLinearSystem.MoodVertex, EmotionLinearSystem.MoodEdge> buildGraph() {
//        ContiguousSet.create(Range.closed(0, 7), DiscreteDomain.integers());
        EmotionSystemBuilder<MoodVertex, MoodEdge> builder = new EmotionSystemBuilder<>()
            .addVertexProperty("const")
                .add(0, 0.0) 
                .add(1, 0.0)
                .add(2, 0.0)
                .add(3, 0.0)
                .add(4, 0.0)
                .add(5, 0.0)
                .add(6, 0.0)
                .add(7, 0.0)
            .build()
            .addEdgeProperty("alpha")
                .add(0, 1, 0.0) // +
                .add(0, 7, 0.0) // +
                .add(0, 4, 0.0) // --
                .add(0, 6, 0.0) // - 
                
                .add(1, 2, 0.0) // - 
                .add(1, 4, 0.0) // -
                .add(1, 5, 0.0) // --
                
                .add(2, 3, 0.0) // +
                .add(2, 5, 0.0) // +
                .add(2, 6, 0.0) // +
                
                .add(3, 7, 0.0) // +
                
                .add(4, 7, 0.0) // - 
                
                .add(5, 6, 0.0) // + 
            .build()
            .addEdgeProperty("lambda")
                .add(0, 1, 0.0)
                .add(0, 7, 0.0)
                .add(0, 4, 0.0)
                .add(0, 6, 0.0)
                
                .add(1, 2, 0.0)
                .add(1, 4, 0.0)
                .add(1, 5, 0.0)
                
                .add(2, 3, 0.0)
                .add(2, 5, 0.0)
                .add(2, 6, 0.0)
                
                .add(3, 7, 0.0)
                
                .add(4, 7, 0.0)
                
                .add(5, 6, 0.0)
            .build();
        
        builder.modifyVertices("const")
                .apply(player::getTraits)
                .accept(vals -> vals.get3());
        
        builder.modifyEdges("lambda")
                .apply(player::getTraits)
                .accept(vals -> vals.get4());
        
        builder.modifyEdges("alpha")
                .apply(player::getTraits)
                .accept(vals -> vals.get4());
        
        return builder.build((id, fn) -> {
            return new MoodVertex(id, fn.applyAsDouble("const"));
        }, fn -> {
            return new MoodEdge(fn.applyAsDouble("lambda"), fn.applyAsDouble("alpha"));
        });
    }
}
