package edu.cmu.cs464.p3.modulelang.ast

/**
 * @author zkieda
 */

abstract class ImportToken extends Node 
case class Star() extends ImportToken {
  override def toString() = "*"
}
case class Ident(id : String) extends ImportToken {
  override def toString() = id
}


object ImportToken{
  def star() : ImportToken = 
    Star()
  
  def ident(id : String) : ImportToken = 
    Ident(id)
}