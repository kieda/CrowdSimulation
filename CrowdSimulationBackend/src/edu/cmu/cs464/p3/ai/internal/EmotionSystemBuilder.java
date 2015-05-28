package edu.cmu.cs464.p3.ai.internal;

import com.google.common.base.Functions;
import com.google.common.collect.Maps;
import edu.cmu.cs464.p3.util.Tuple;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;
import org.jgrapht.EdgeFactory;
import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleGraph;

/**
 * Generates the initial emotional system that is used by the emotion linear 
 * system.
 * 
 * @author zkieda
 */
public class EmotionSystemBuilder<V, E> {
    private Map<String, VertexProperty> vertexProperties = new HashMap<>();
    private Map<String, EdgeProperty> edgeProperties = new HashMap<>();
    
    public class VertexProperty{
        private final String key;
        private final Map<Integer, Double> vertices;
        private VertexProperty(String key){
            this.key = key;
            this.vertices = new HashMap<>(1);
        }
        
        public VertexProperty add(int j, double value){
            vertices.put(j, value);
            return this;
        }
        public EmotionSystemBuilder build(){
            return EmotionSystemBuilder.this;
        }
    }
    
    public VertexProperty addVertexProperty(String propertyName){
        return vertexProperties.putIfAbsent(propertyName, new VertexProperty(propertyName));
    }
    public EdgeProperty addEdgeProperty(String propertyName){
        return edgeProperties.putIfAbsent(propertyName, new EdgeProperty(propertyName));
    }
    
    public class EdgeProperty{
        
        private final String key;
        private final Map<Tuple.Tuple2<Integer, Integer>, Double> edges;
        private EdgeProperty(String key) {
            this.key = key;
            this.edges = new HashMap<>(2);
        }
        
        public EdgeProperty add(int j, int i, double value){
            edges.put(new Tuple.Tuple2<>(j, i), value);
            return this;
        }
        public EmotionSystemBuilder build(){
            return EmotionSystemBuilder.this;
        }
    }
    /**
     * modifies edges under this property name. 
     *  modifyEdges (property) (supplier) (fn)
     * will take all edges with the given "property"
     * supplier will provide fn with some value T
     * and fn(t, j, i, val) = val' provides a transformation such that
     *  val = property[j, i]
     *  t = supplier()
     *  (j, i) = current edge
     *  val' is our new property such that
     *  property[j, i] := val'
     */
    public <T> Function<Supplier<T>, Consumer<Function<Tuple.Tuple4<T, Integer, Integer, Double>, Double>>> modifyEdges(String property){
        if(!edgeProperties.containsKey(property))
            return supplier -> fn -> {};
        
        return supplier -> fn -> {
            Maps.transformEntries(edgeProperties.get(property).edges, 
                (edge, val) -> fn.apply(new Tuple.Tuple4<>(supplier.get(), edge.get1(), edge.get2(), val)));
        };
    }
    
    //lol java functional
    public <T> Function<Supplier<T>, Consumer<Function<Tuple.Tuple3<T, Integer, Double>, Double>>> modifyVertices(String property){
        if(!vertexProperties.containsKey(property)) 
            return supplier -> fn -> {};
        
        return supplier -> fn -> {
            Maps.transformEntries(vertexProperties.get(property).vertices, 
                (id, val) -> fn.apply(new Tuple.Tuple3<>(supplier.get(), id, val)));
        };
    }
    public Graph<V, E> build(
            BiFunction<Integer, ToDoubleFunction<String>, V> vertexFn, 
            Function<ToDoubleFunction<String>, E> edgeFn){
        return build(vertexFn, edgeFn, (v1, v2) -> {throw new IllegalStateException();});
    }
    
    //1. (String, double) map -> vertex
    //2. (String, double) map -> edge
    public Graph<V, E> build(
            BiFunction<Integer, ToDoubleFunction<String>, V> vertexFn, 
            Function<ToDoubleFunction<String>, E> edgeFn, 
            EdgeFactory<V, E> defaultEdgeFactory){
        Map<Integer, Map<String, Double>> vertices = new HashMap<>();
        Map<Tuple.Tuple2<Integer, Integer>, Map<String, Double>> edges = new HashMap<>();
        //0. create maps
        vertexProperties.forEach((property, vertex) -> {
            vertex.vertices.forEach((id, val) -> vertices.computeIfAbsent(id, i -> new HashMap<>(1)).put(property, val));
        });
        edgeProperties.forEach((property, edge) -> {
            edge.edges.forEach((e, val) -> 
                edges.computeIfAbsent(e, i -> new HashMap<>(2)).put(property, val));
        });
        
        Map<Integer, V> verts = new HashMap<>();
        
        //1. create graph. We add edges manually here.
        Graph<V, E> re = new SimpleGraph<>(defaultEdgeFactory);
        
        //2. populate graphs by creating each vertex found.
        vertices.forEach((id, properties) -> {
            V vert = vertexFn.apply(id, properties::get);
            verts.put(id, vert);
            re.addVertex(vert);
        });
        
        edges.forEach((edge, properties) -> 
            re.addEdge(verts.get(edge.get1()), verts.get(edge.get2()), edgeFn.apply(properties::get)));
        return re;
    }
}
