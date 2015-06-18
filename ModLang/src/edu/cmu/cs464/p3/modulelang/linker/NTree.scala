package edu.cmu.cs464.p3.modulelang.linker

import java.lang.{Class => JClass};
import java.util.function.{Function => JFun};
import java.util.function.{BiFunction => JFun2};
import java.util.{List => JList};

/**
 * @author zkieda
 */
case class NTree[T](c : T, var children : List[NTree[T]]);

object NTree{
  /**
   * f : transform our result of g, and the recursive result of all children
   * in the traversal to 'a (collect function)
   * g : change a class to a type 
   */
  def traverseUp[A, B](f : (List[A], A) => A, g : B => A)(n : NTree[B]): A = 
    n match {
      case NTree(c, children) => 
        val bs = children.map (traverseUp (f, g) _)
        f(bs, g(c))
    }
  
  /**
   * java accessor for upwards traversal. 
   */
  def traverseUp[A, B](f : JFun2[List[A], A, A], g : JFun[B, A]) : JFun[NTree[B], A] = {
    class J extends JFun[NTree[B], A]{
      override def apply(b : NTree[B]) = traverseUp ((L : List[A], l : A) => f.apply(L, l), (v : B) => g.apply(v)) (b)
    }
    new J()
  }
}