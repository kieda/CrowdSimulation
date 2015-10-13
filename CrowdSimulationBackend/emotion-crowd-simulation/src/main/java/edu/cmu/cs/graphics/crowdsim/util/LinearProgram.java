package edu.cmu.cs.graphics.crowdsim.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.ejml.ops.CommonOps;
import org.ejml.simple.SimpleMatrix;

/**
 * We use Linear Program loosely here. We just describe a constrained system
 * and its properties here.
 * @author zkieda
 */
public class LinearProgram {
    private List<MatrixConstraint> constraints = new ArrayList<>();
    private abstract static class MatrixConstraint{
        private final SimpleMatrix mat;
        private final SimpleMatrix vec;

        public MatrixConstraint(SimpleMatrix mat, SimpleMatrix vec) {
            this.mat = mat;
            this.vec = vec;
        }
        abstract boolean satisfy(SimpleMatrix vec);
        abstract Constraint mkConstraint(int rowNo);
    }
    public abstract static class Constraint{
        private final MatrixConstraint constraint;
        private final int rowNo;

        public Constraint(MatrixConstraint constraint, int rowNo) {
            this.constraint = constraint;
            this.rowNo = rowNo;
        }
        
        /**
         * @return the normal vector to this constraint plane
         */
        public SimpleMatrix getPlaneNormal(){
            return constraint.mat.extractVector(true, rowNo);
        }
        public double getConstraintValue(){
            return constraint.vec.get(rowNo, 0);
        }
        
        
        /**
         * @param vec a initial starting point
         * @param delta the slope of the vector
         * @return the distance from this line segment to this plane. If the 
         * plane is pependicular, either +Inf or -Inf is returned
         */
        public double intersectionDistance(SimpleMatrix vec, SimpleMatrix delta){
            final SimpleMatrix norm = getPlaneNormal();
            
             return (getConstraintValue() - norm.dot(vec)) / 
                     (norm.dot(delta));
             
        }
        
        /**
         * @param delta the slope of the line segment
         * @return true iff a ray that starts inside the polytope with slope
         * {@param delta} will eventually reach this constraint plane
         */
        public boolean willIntersect(SimpleMatrix delta){
            return getPlaneNormal().dot(delta) < 0;
        }
        
        public abstract boolean satisfy(double another);
    }
    public void addLEMatrixConstraints(final SimpleMatrix mat, final SimpleMatrix vec){
        constraints.add(new MatrixConstraint(mat, vec) {
            @Override
            boolean satisfy(SimpleMatrix otherVec) {
                return MatrixUtil.compare(mat.mult(otherVec), vec, (a, b) -> a <= b);
            }

            @Override
            Constraint mkConstraint(int rowNo) {
                return new Constraint(this, rowNo) {
                    @Override
                    public boolean satisfy(double another) {
                        return another <= getConstraintValue();
                    }
                };
            }
        });
    }
    
    public void addGEMatrixConstraints(SimpleMatrix mat, SimpleMatrix vec){
        constraints.add(new MatrixConstraint(mat, vec) {
            
            @Override
            boolean satisfy(SimpleMatrix otherVec) {
                return MatrixUtil.compare(mat.mult(otherVec), vec, (a, b) -> a >= b);
            }

            @Override
            Constraint mkConstraint(int rowNo) {
                return new Constraint(this, rowNo) {
                    @Override
                    public boolean satisfy(double another) {
                        return another >= getConstraintValue();
                    }
                };
            }
            
        });
    }
    
    public Iterator<Constraint> getConstraints(){
        return new Iterator<Constraint>() {
            private int currentMat = 0;
            private int currentRow = 0;
            @Override
            public boolean hasNext() {
                return (currentMat < constraints.size()) &&
                   (currentRow < constraints.get(currentMat).mat.numRows());
            }

            @Override
            public Constraint next() {
                Constraint c = constraints.get(currentMat).mkConstraint(currentRow);
                if(currentRow >= constraints.get(currentMat).mat.numRows()){
                    currentMat ++;
                    currentRow = 0;
                } else currentRow++;
                return c;
            }
        };
    }
    
    //tests if this point is inside the LP
    public boolean isFeasiblePoint(SimpleMatrix vec){
        return constraints.stream().allMatch(
            constraint -> constraint.satisfy(vec));
    }
}
