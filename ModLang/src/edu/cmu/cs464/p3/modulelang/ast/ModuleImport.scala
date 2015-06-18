package edu.cmu.cs464.p3.modulelang.ast
import java.util.{List => JList};

/**
 * @author zkieda
 */
case class ModuleImport(path: JList[ImportToken]) extends Node{
  def isPackageImport : Boolean = path.get(path.size() - 1).isInstanceOf[Star];
  def length =  path.size() - {if(isPackageImport) 1 else 0}
  def willImport (javaRef : JavaRef): Boolean = 
    javaRef.isSingle && isPackageImport || {
       path.get(length - 1).toString().equals(javaRef.path.get(0))
    }
}