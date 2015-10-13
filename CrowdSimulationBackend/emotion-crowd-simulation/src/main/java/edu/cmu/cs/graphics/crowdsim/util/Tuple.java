package edu.cmu.cs.graphics.crowdsim.util;

import java.util.Objects;

/**
 * made since java doesn't natively support tuples?
 * @author zkieda
 */
public class Tuple {
    private Tuple(){}
    
    public static class Tuple2<A, B> extends Tuple{
        private final A val1;
        private final B val2;
        public Tuple2(A val1, B val2){
            this.val1 = val1;
            this.val2 = val2;
        }

        public A get1() {
            return val1;
        }

        public B get2() {
            return val2;
        }

        @Override
        public int hashCode() {
            return Objects.hash(get1(), get2());
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Tuple2<?, ?> other = (Tuple2<?, ?>) obj;
            if (!Objects.equals(this.val1, other.val1)) {
                return false;
            }
            if (!Objects.equals(this.val2, other.val2)) {
                return false;
            }
            return true;
        }

        
    }
    
    public static class Tuple3<A, B, C> extends Tuple2<A, B>{
        private final C val3;
        public Tuple3(A val1, B val2, C val3) {
            super(val1, val2);
            this.val3 = val3;
        }

        public C get3() {
            return val3;
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), get3());
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Tuple3<?, ?, ?> other = (Tuple3<?, ?, ?>) obj;
            if (!Objects.equals(this.val3, other.val3)) {
                return false;
            }
            return super.equals(obj);
        }
        
    }
    
    public static class Tuple4<A, B, C, D> extends Tuple3<A, B, C>{
        private final D val4;
        public Tuple4(A val1, B val2, C val3, D val4) {
            super(val1, val2, val3);
            this.val4 = val4;
        }

        public D get4() {
            return val4;
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), get4());
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Tuple4<?, ?, ?, ?> other = (Tuple4<?, ?, ?, ?>) obj;
            if (!Objects.equals(this.val4, other.val4)) {
                return false;
            }
            return super.equals(obj);
        }
    }
}
