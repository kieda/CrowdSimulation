package edu.cmu.cs.graphics.crowdsim.util;

/**
 * @author zkieda
 */
@FunctionalInterface
public interface PointToRealFn {
    double eval(double x, double y);
}
