package edu.cmu.cs464.p3.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Stack;

/*************************************************************************
 *  Compilation:  javac QuadTree.java
 *  Execution:    java QuadTree M N
 *
 *  Quad tree.
 * 
 *************************************************************************/

public class QuadTree<Value>  {
    Node root;

    // helper node data type
    class Node {
        double x, y;              // x- and y- coordinates
        Node NW, NE, SE, SW;   // four subtrees
        Value value;           // associated data

        Node(double x, double y, Value value) {
            this.x = x;
            this.y = y;
            this.value = value;
        }
    }


  /***********************************************************************
    *  Insert (x, y) into appropriate quadrant
    ***********************************************************************/
    public void insert(double x, double y, Value value) {
        root = insert(root, x, y, value);
    }

    private Node insert(Node h, double x, double y, Value value) {
        if (h == null) return new Node(x, y, value);
        //// if (eq(x, h.x) && eq(y, h.y)) h.value = value;  // duplicate
        else if ( less(x, h.x) &&  less(y, h.y)) h.SW = insert(h.SW, x, y, value);
        else if ( less(x, h.x) && !less(y, h.y)) h.NW = insert(h.NW, x, y, value);
        else if (!less(x, h.x) &&  less(y, h.y)) h.SE = insert(h.SE, x, y, value);
        else if (!less(x, h.x) && !less(y, h.y)) h.NE = insert(h.NE, x, y, value);
        return h;
    }

    
    
  /***********************************************************************
    *  Range search.
    ***********************************************************************/

    public List<Value> query2D(Interval2D rect) {
        Deque<Value> res = new ArrayDeque<>();
        query2D(root, rect, res);
        return new ArrayList<>(res);
    }

    private void query2D(Node h, Interval2D rect, Deque<Value> A) {
        if (h == null) return;
        double xmin = rect.intervalX.low;
        double ymin = rect.intervalY.low;
        double xmax = rect.intervalX.high;
        double ymax = rect.intervalY.high;
        if (rect.contains(h.x, h.y)) A.add(h.value);
        if ( less(xmin, h.x) &&  less(ymin, h.y)) query2D(h.SW, rect, A);
        if ( less(xmin, h.x) && !less(ymax, h.y)) query2D(h.NW, rect, A);
        if (!less(xmax, h.x) &&  less(ymin, h.y)) query2D(h.SE, rect, A);
        if (!less(xmax, h.x) && !less(ymax, h.y)) query2D(h.NE, rect, A);
    }


   /*************************************************************************
    *  helper comparison functions
    *************************************************************************/

    private boolean less(double k1, double k2) { return k1< k2; }
    private boolean eq  (double k1, double k2) { return k1== k2; }

}