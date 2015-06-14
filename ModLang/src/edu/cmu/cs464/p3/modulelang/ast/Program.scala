package edu.cmu.cs464.p3.modulelang.ast

import java.util.{List => JList};

/**
 * @author zkieda
 */
case class Program(imports : JList[ModuleImport], baseModule : ParsedModule) extends Node{}