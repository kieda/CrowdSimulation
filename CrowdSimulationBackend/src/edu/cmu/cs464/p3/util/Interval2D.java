package edu.cmu.cs464.p3.util;

import javafx.geometry.Point2D;


/*************************************************************************
 *  Compilation:  javac Interval2D.java
 *  Execution:    java Interval2D
 *  
 *  2-dimensional interval data type.
 *
 *************************************************************************/

/**
 *  The <tt>Interval2D</tt> class represents a closed two-dimensional interval,
 *  which represents all points (x, y) with both xleft <= x <= xright and
 *  yleft <= y <= right.
 *  Two-dimensional intervals are immutable: their values cannot be changed
 *  after they are created.
 *  The class <code>Interval2D</code> includes methods for checking whether
 *  a two-dimensional interval contains a point and determining whether
 *  two two-dimensional intervals intersect.
 *  <p>
 *  For additional documentation, see <a href="/algs4/12oop">Section 1.2</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 * 
 *  changes : changed comparable to double for the sake of lengths and distances
 *  needed for this project
 */
public class Interval2D {
    public final Interval intervalX;
    public final Interval intervalY;

    /**
     * Initializes a two-dimensional interval.
     * @param x the one-dimensional interval of x-coordinates
     * @param y the one-dimensional interval of y-coordinates
     */
    public Interval2D(Interval x, Interval y) {
        this.intervalX = x;
        this.intervalY = y;
    }

    /**
     * Does this two-dimensional interval intersect that two-dimensional interval?
     * @param that the other two-dimensional interval
     * @return true if this two-dimensional interval intersects
     *    that two-dimensional interval; false otherwise
     */
    public boolean intersects(Interval2D that) {
        if (!this.intervalX.intersects(that.intervalX)) return false;
        if (!this.intervalY.intersects(that.intervalY)) return false;
        return true;
    }

    /**
     * Does this two-dimensional interval contain the point p?
     * @param p the two-dimensional point
     * @return true if this two-dimensional interval contains the point p; false otherwise
     */
    public boolean contains(double xPoint, double yPoint) {
        return intervalX.contains(xPoint)  && intervalY.contains(yPoint);
    }
    
    /**
     * Returns a string representation of this two-dimensional interval.
     * @return a string representation of this two-dimensional interval
     *    in the form [xleft, xright] x [yleft, yright]
     */
    public String toString() {
        return intervalX + " x " + intervalY;
    }
}