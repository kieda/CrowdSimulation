package edu.cmu.cs464.p3.modulelang.linker

import edu.cmu.cs464.p3.modulelang.ast.Program
import edu.cmu.cs464.p3.modulelang.ast.Node
import edu.cmu.cs464.p3.modulelang.ast.ModuleImport
import edu.cmu.cs464.p3.modulelang.ast.ParsedModule
import edu.cmu.cs464.p3.modulelang.ast.JavaRef

/**
 * @author zkieda
 */
class Linker
object Linker {
  
  private def tryGetClass (handle : ClassNotFoundException => Class[_]) (s : JavaRef) = 
    try{
      Class.forName(s.toString(), false, getClass().getClassLoader) 
    } catch {
      case e : ClassNotFoundException => 
        handle(e) 
    } 
      
  private def genImports(L : List[ModuleImport]) : (JavaRef => Class[_]) = 
    L.foldRight ((j : JavaRef) => tryGetClass (e => 
      throw new LinkingException("Java reference " + j + " not found.", e)) (j)
      ) ((moduleImport, f) =>
        j => {
          if(moduleImport.willImport(j)) tryGetClass (_ => f(j)) (j.setPackage(moduleImport))
          else f(j)
        }
      );
  
  private def construct (f : JavaRef => Class[_]) (n : Node) : NTree[Class[_]] = n match {
    
    case ParsedModule(baseClass, children) => 
      var L : List[NTree[Class[_]]] = List()
      for(i <- (0 until children.size())) {
        L = construct (f) (children.get(i))::L 
      }
      NTree(f(baseClass), L)
      
      //never visited
//    case Program(imports, module) => ???
//    case ModuleImport(path) => ???
//    case JavaRef(path) => ???
  }
  
  def link(p : Program) : NTree[Class[_]]= p match {
    case Program(imports, module) => 
      var L : List[ModuleImport] = List()
      for(i <- (0 until imports.size())) {
        L = imports.get(i)::L 
      }
      construct (genImports(L)) (module)
  }
}