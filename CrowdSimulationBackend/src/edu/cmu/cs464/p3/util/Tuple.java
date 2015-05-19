package edu.cmu.cs464.p3.util;

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
    }
}
