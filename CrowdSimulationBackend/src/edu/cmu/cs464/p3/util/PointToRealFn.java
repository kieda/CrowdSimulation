package edu.cmu.cs464.p3.util;

/**
 * @author zkieda
 */
@FunctionalInterface
public interface PointToRealFn {
    double eval(double x, double y);
}
