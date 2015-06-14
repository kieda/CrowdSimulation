package edu.cmu.cs464.p3.modulelang.ast
import java.util.{List => JList}
import java.util.stream.Collectors
/**
 * @author zkieda
 */
case class JavaRef(path : JList[String]) extends Node{
  private def ref : String = path.stream().collect(Collectors.joining(", "))
  override def toString() = ref
}
