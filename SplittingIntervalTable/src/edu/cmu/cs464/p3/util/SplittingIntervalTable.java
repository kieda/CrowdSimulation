package edu.cmu.cs464.p3.util;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

/**
 * note. what we can do is just
 *      1. obtain an ordering on the quad-tree based on distance
 *      2. obtain an ordering on the quad-tree based on theta. The ordering
 *         is based on the left hand point.
 * 
 * Something in front will theta T1 will always start earlier than 
 * something in back with theta T2 >= T1
 * 
 * Front will 
 */

/**
 * Different version of the SplittingIntervalTable that allows us to specify
 * an insertion and length. This data structure has a modified insertion 
 * operation - insertBehind, which inserts over a position and length. 
 * insertBehind will insert in any intervals from pos to pos + length that
 * are not yet covered with an interval. 
 * 
 * @author zkieda
 */
public class SplittingIntervalTable<T> {
    static class IntervalBase<S>{
        
        private IntervalBase(double lo, double hi, Empty<S> next) {
            this.hi = hi;
            this.lo = lo;
            this.nextEmpty = next;
        }
        
        private double hi, lo;
        private Empty<S> nextEmpty;
        public double getHi() {
            return hi;
        }

        public double getLo() {
            return lo;
        }

        public double getLength(){
            return hi - lo;
        }

        public boolean contains(double val){
            return lo <= val && val <= hi;
        }
        
        Empty<S> getNextEmpty(){
            return nextEmpty;
        }
        
        boolean isEmpty(){
            return this instanceof Empty;
        }
        
        //insert a new interval into this linked list.
//        void insert(IntervalBase<S> next){
//            assert next.getNextEmpty() == null;
//            next.next = this.next;
//            this.next = next;
//        }
    }
    
    public static class Interval<S> extends IntervalBase<S>{
        private Interval(double lo, double hi, Empty<S> nextEmpty, S value) {
            super(lo, hi, nextEmpty);
            this.value = value;
        }
        
        private S value;

        public S getValue() {
            return value;
        }
    }
    
    public static class Empty<S> extends IntervalBase<S>{
        private Empty(double lo, double hi, Empty<S> nextEmpty) {
            super(lo, hi, nextEmpty);
        }
    }
    
    //this is used to update multiple previous intervals when a single interval 
    // is deleted. We have several 
    
    // PLAN : 
    
    // 8 : 7 + 7/2 + 7/4
    // n : n + n/2 + n/4 + == c * n 
    
    // 2^k - 1 = 2^0 + 2^1 + ... 2^{k-1}
    // 2^k : 2^0 * 2^{k-1} + 2^1 * 2^{k-2} +  ... 2^{k-1} 2^{0}
    //     2^{k}-1 insertions
    //     (k-1) * 2^{k-1} work
    //     time : k-1 per operation, in log(2^k)
    // constant time when we traverse to NEAREST empty interval
    
    private class IntervalRef<S> {
        private IntervalBase<S> interval;
    }
    
    private class Bucket<S> extends TreeMap<Double, IntervalBase<S>>{
        private final int bucketNo;
        public Bucket(int bucketNo) {
            this.bucketNo = bucketNo;
        }

        private int getBucketNo() {
            return bucketNo;
        }
    }
    
    
    
    //double represents left hand point of an interval.
    //when searching for a point, get 
    private Bucket<T>[] buckets;
    
    private final double hi, lo;
    private final double intervalLength;
    private int count;
    
    public SplittingIntervalTable(double hi, double lo){
        this(hi, lo, 8);
    }
    
    public SplittingIntervalTable(double hi, double lo, int initialBucketCount){
        this.hi = hi;
        this.lo = lo;
        this.intervalLength = hi - lo;
        buckets = new Bucket[initialBucketCount];
        
        //we have one empty interval initially that goes from 
        //lo to hi inclusively. Each bucket contains this interval.
        
        Empty e = new Empty(lo, hi, null);
        Arrays.setAll(buckets, i -> {
            Bucket<T> b = new Bucket<>(i);
            b.put(lo, e);
            return b;
        });
    }
    
    // *** convenience methods ***
    
    private IntervalBase<T> search(Bucket<T> bucket, double key){
        //no need to check if 
        //      1) floorEntry is non null, since bucket will be filled
        //      2) that the resulting range contains the key, since we 
        //         have the invariant that all intervals are covered.
        return bucket.floorEntry(key).getValue();
    }
    
    /**
     * @return current bucket length
     */
    private double bucketLength(){
        return intervalLength / buckets.length;
    }
    
    /**
     * @return correct bucket that point corresponds to 
     */
    private int hash(double point){
        return point == hi ? buckets.length - 1 : (int)(point / bucketLength());
    }
    
    /**
     * Re-create this SIT over the same interval but with new buckets.
     */
    private void refactor(int bucketCount){
        List<Interval<T>> l = getRange(lo, hi);
        Arrays.fill(buckets, null);
        buckets = new Bucket[bucketCount];
        for(int i = l.size(); i >= 0; i--){
            Interval<T> in = l.get(i);
            insertBehind(in.getLo(), in.getLength(), in.getValue());
        }
    }
    
    /**
     * @return the bucket at point p
     */
    private Bucket<T> getBucket(double p){
        return buckets[hash(p)];
    }
    
    /**
     * @return the interval at point p
     */
    private IntervalBase<T> getInterval(double p){
        return search(getBucket(p), p);
    }
    
    // maybe 
    //   private class Bucket(int bucketNo)
    
    //then we can calculate the least bucketLeftHandSide, 
    //bucketRightHandSide easily.
    
    // this will grab us the least point that's actually inside of this bucket's
    // interval
    
//    /**
//     * @return either the least interval in this bucket or the 
//     */
//    private double min(Bucket<T> bucket, double lo){
//        Interval<T> least = bucket.firstEntry().getValue();
//        if(least.lo <= lo) return lo;
//        else return least.hi;
//    }
    
    /**
     * @param p checks that p is a valid insertion, and refactors the data
     * structure if necessary
     */
    private void checkInsert(double p){
        if(lo > p || hi < p)
            throw new IllegalArgumentException("Cannot insert point " + p + " into interval [" + lo + ", " + hi + "].");
        if(count >= 2 * buckets.length) refactor(count);
    }
    
    // *** public methods *** 
    private void checkRange(double i, double j){
        if(j < i || i < lo || j > hi)
            throw new IllegalArgumentException("Invalid range [" + i + ", " + j + "] on interval [" + lo + ", " + hi + "].");
    }
    /**
     * @return all intervals that overlap with the range (i, j)
     */
    public List<Interval<T>> getRange(double i, double j){
        checkRange(i, j);
        
        List<Interval<T>> re = new ArrayList<>();
        
        //todo get starting index, ending index.
        //cut out intervals not in (i, j)
        
        IntervalBase<T> interval = getInterval(i);
        while(interval.lo <= j){
            if(!interval.isEmpty()) re.add((Interval<T>)interval);
            interval = interval.getNext();
        }
        
        return re;
    }
    private void insertInterval(Bucket<T> b, IntervalBase<T> interval){
        b.put(interval.lo, interval);
    }
    /**
     * invariant : getInterval(p).isEmpty()
     */
    private void insertBehindH(double p, double q, T t){
        Bucket<T> bucket = getBucket(p);
        IntervalBase<T> interval = search(bucket, p);
        
        // does the interval we start in begin at p?
        boolean startSame = p == interval.getLo();

        // does the empty interval that we are inserting into 
        // end before than the new interval's ending?
        boolean endEarly = interval.getHi() >= q;

        if(endEarly){
            //insert here. Modify existing empty interval.
            
            
            if(startSame){
                //starts are the same and we end early. This implies that
                //interval is completely written over. 
                interval
                
                Interval<T> insertion = new Interval<>(p, interval.hi, interval.getNextEmpty(), t);
                insertInterval(bucket, insertion);
            } else {
                double hi = interval.hi;
                interval.hi = p;

                Interval<T> insertion = new Interval<>(p, hi, interval.getNextEmpty(), t);
                insertInterval(bucket, insertion);
                //todo insert over all relevant buckets


            }
            
            if(hi == q){
                // we stop here and do not continue to insert into the
                // next empty interval.
                return;
            }
            
            // continue to insert into this interval
            interval = interval.getNextEmpty();
            if(interval == null) return;
            
            //insert again on the next empty interval.
            insertBehindH(interval.getLo(), q, t);
        } else{
            // insert a new empty interval.
            // this is the final interval that we insert. 
            if(startSame){
                Interval<T> insert = new Interval(p, q, (Empty<T>)interval, t);
                
                bucket.remove(interval.getLo());
                
                interval.lo = q;
                
                //todo update the rest of the intervals 
                // (removing from buckets overwritten by new interval,
                //  inserting new interval into each)
                
                insertInterval(bucket, insert);
                insertInterval(bucket, interval);
            } else{
                Empty<T> empty = new Empty<>(q, interval.hi, interval.getNextEmpty());
                Interval<T> insert = new Interval<>(p, q, empty, t);
                interval.hi = p;
                interval.nextEmpty = empty;
                
                insertInterval(bucket, empty);
                insertInterval(bucket, insert);
            }
        }
    }
    
    /**
     * Inserts interval (p, q) behind all other existing intervals.
     */
    public void insertBehind(double p, double q, T t) {
        checkRange(p, q);
        checkInsert(p);
        
        IntervalBase<T> interval = getInterval(p);
        
        //if we're inserting into an already filled interval, 
        //continue to the next empty one, then insert from the beginning of that
        //one.
        if(!interval.isEmpty()) {
            interval = interval.getNextEmpty();
            
            //if our interval is completely unseen, do nothing.
            if(interval == null || interval.getLo() >= q) return;
        }
        
        insertBehindH(p, q, t);
    }
    
    /**
     * @return the item stored at point p
     */
    public T get(double p){
        IntervalBase<T> i = getInterval(p);
        return i.isEmpty()? null : ((Interval<T>)i).getValue();
    }
    
    /**
     * @return true iff point p maps to an inserted interval 
     */
    public boolean contains(double p){
        IntervalBase i = getInterval(p);
        return !i.isEmpty();
    }
    
    /**
     * @return current number of inserted intervals in the data structure
     */
    public int size(){
        return count;
    }
}
