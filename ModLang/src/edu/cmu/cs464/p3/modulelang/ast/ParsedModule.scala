package edu.cmu.cs464.p3.modulelang.ast

import java.util.{List => JList}

/**
 * @author zkieda
 */
case class ParsedModule(baseClass : JavaRef, children : JList[ParsedModule]) extends Node{}