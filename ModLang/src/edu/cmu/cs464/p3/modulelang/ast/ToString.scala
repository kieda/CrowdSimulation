package edu.cmu.cs464.p3.modulelang.ast

import java.util.stream.Collectors

/**
 * Utility for printing our ast
 * @author zkieda
 */
object ToString {
  private def toString (x: Node, i : Int) : String = {
    val spacing::_ = if(i == 0) List("") else List.iterate("", i)((spacing:String) => spacing + "  ").reverse
    
    x match {
      case Program(imports, baseModule) => 
        (if(!imports.isEmpty()) 
          spacing + "import\n" + 
          {
            val S = List.tabulate(imports.size)(j => toString(imports.get(j), i+1) + "\n")
            
            S.mkString("")
          } + 
          spacing + "end\n" else "") + toString(baseModule, i+1) + "\n"
      case JavaRef(path) => path.stream().collect(Collectors.joining("."))
      case ModuleImport(path) => {
        var L : List[String] = List()
        for(j <- 0 until path.size()){
          L = toString(path.get(j), i+1)::L
        }
        spacing + "  " + L.reverse.mkString(".");
      }
      case ParsedModule(baseClass, children) => 
        toString(baseClass, i+1) + (
          children.size() match {
            case 0 => ""
            case 1 => ":" + toString(children.get(0), i)
            case _ => ":\n" + spacing + "has\n" + {
               var L : List[String] = List()
               for(j <- 0 until children.size()) L = (spacing + "  "+ toString(children.get(j), i+1))::L
               L.reverse.mkString("\n")
            } + "\n" + spacing + "end"
          }
      )
        
      case Star() => "*"
      case Ident(id) => id
    }
  }
  
  def genString (x : Node) : String = toString(x, 0)
}