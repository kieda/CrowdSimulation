package edu.cmu.cs464.p3.util;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.ejml.simple.SimpleMatrix;
import edu.cmu.cs464.p3.util.Tuple.*;
import org.ejml.data.MatrixIterator64F;
import org.ejml.ops.MatrixDimensionException;
/**
 * @author zkieda
 */
public class MatrixUtil {
    public static void fill(SimpleMatrix mat, BiFunction<Integer, Integer, Double> posToValue){
        for(int j = 0; j < mat.numRows(); j++){
            for(int i = 0; i < mat.numCols(); i++){
                mat.set(j, i, posToValue.apply(j, i));
            }
        }
    }
    
    public static boolean compare(SimpleMatrix m1, SimpleMatrix m2, BiPredicate<Double, Double> cmpFn){
        if(m1.numCols() == m2.numCols() && m1.numRows() == m2.numRows()){
            
            Iterator<Double> it1 = m1.iterator(true, 0, 0, m1.numRows() - 1, m1.numCols() - 1);
            Iterator<Double> it2 = m2.iterator(true, 0, 0, m2.numRows() - 1, m2.numCols() - 1);
            for(double d : (Iterable<Double>)(() -> it1)){
                if(!cmpFn.test(d, it2.next())) return false;
            }
            return true;
        } else throw new MatrixDimensionException();
    }
    
    public static Stream<Tuple3<Integer, Integer, Double>> stream(SimpleMatrix mat){
        Iterator<Tuple3<Integer, Integer, Double>> it = new Iterator<Tuple3<Integer, Integer, Double>>() {
            private MatrixIterator64F delegate = mat.iterator(true, 0, 0, mat.numRows() - 1, mat.numCols() - 1);
            @Override
            public boolean hasNext() {
                return delegate.hasNext();
            }

            @Override
            public Tuple3<Integer, Integer, Double> next() {
                double val = delegate.next();
                int idx = delegate.getIndex();
                
                return new Tuple3<>(getRow(mat, idx), getCol(mat, idx), val);
            }
        };
        return toStream(it);
    }
    
    public static <T> Stream<T> toStream(Iterator<T> it){
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(it, Spliterator.ORDERED), false);
    }
    
    public static <A, B, C> Stream<C> zip(Stream<? extends A> a,
            Stream<? extends B> b,
            BiFunction<? super A, ? super B, ? extends C> zipper) {
        Spliterator<A> aSpliterator = (Spliterator<A>) a.spliterator();
        Spliterator<B> bSpliterator = (Spliterator<B>) b.spliterator();

        // Zipping looses DISTINCT and SORTED characteristics
        int both = aSpliterator.characteristics() & bSpliterator.characteristics()
                & ~(Spliterator.DISTINCT | Spliterator.SORTED);
        int characteristics = both;

        long zipSize = ((characteristics & Spliterator.SIZED) != 0)
                ? Math.min(aSpliterator.getExactSizeIfKnown(), bSpliterator.getExactSizeIfKnown())
                : -1;

        Iterator<A> aIterator = Spliterators.iterator(aSpliterator);
        Iterator<B> bIterator = Spliterators.iterator(bSpliterator);
        Iterator<C> cIterator = new Iterator<C>() {
            @Override
            public boolean hasNext() {
                return aIterator.hasNext() && bIterator.hasNext();
            }

            @Override
            public C next() {
                return zipper.apply(aIterator.next(), bIterator.next());
            }
        };

        Spliterator<C> split = Spliterators.spliterator(cIterator, zipSize, characteristics);
        return (a.isParallel() || b.isParallel())
                ? StreamSupport.stream(split, true)
                : StreamSupport.stream(split, false);
    }
    public static <A, B> Stream<Tuple2<A, B>> zip(Stream<A> as, Stream<B> bs){
        return zip(as, bs, Tuple2::new);
    }
    
    //column major
    public static int getRow(SimpleMatrix mat, int idx){
        return idx / mat.numCols();
    }
    
    public static int getCol(SimpleMatrix mat, int idx){
        return idx % mat.numCols();
    }
}
