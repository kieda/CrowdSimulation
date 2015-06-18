package edu.cmu.cs464.p3.modulelang.ast
import java.util.{List => JList}
import java.util.stream.Collectors
/**
 * @author zkieda
 */
case class JavaRef(path : JList[String]) extends Node{
  private def ref : String = path.stream().collect(Collectors.joining("."))
  override def toString() = ref
  
  def length = path.size()
  def isSingle : Boolean = length == 1
  def setPackage(j : ModuleImport) : JavaRef = {
    val L = new java.util.ArrayList[String](length + j.length)
    for(i <- 0 until j.length){
      L.add(j.path.get(i).toString())
    }
    if(j.isPackageImport){
      L.addAll(path)
    }
    JavaRef(L)
  }
}
