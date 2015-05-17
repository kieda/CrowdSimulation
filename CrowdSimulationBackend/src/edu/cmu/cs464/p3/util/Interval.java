package edu.cmu.cs464.p3.util;

/**
 * @author zkieda
 */
public class Interval { 
    public final double low;      // left endpoint
    public final double high;     // right endpoint
   
    public Interval(double low, double high) {
        if (less(high, low)) throw new RuntimeException("Illegal argument");
        this.low  = low;
        this.high = high;
    }

    // is x between low and high
    public boolean contains(double x) {
        return !less(x, low) && !less(high, x);
    }

    // does this interval intersect that interval?
    public boolean intersects(Interval that) {
        if (less(this.high, that.low)) return false;
        if (less(that.high, this.low)) return false;
        return true;
    }

    // does this interval equal that interval?
    public boolean equals(Interval that) {
        return this.low == that.low && this.high == that.high;
    }


    // comparison helper functions
    private boolean less(double x, double y) {
        return x < y;
    }

    // return string representation
    public String toString() {
        return "[" + low + ", " + high + "]";
    }
}