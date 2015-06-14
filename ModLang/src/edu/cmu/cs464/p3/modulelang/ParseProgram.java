package edu.cmu.cs464.p3.modulelang;

import edu.cmu.cs464.p3.modulelang.Lexer;
import edu.cmu.cs464.p3.modulelang.Parser;
import edu.cmu.cs464.p3.modulelang.ast.Program;
import edu.cmu.cs464.p3.modulelang.linker.ClassTree;
import edu.cmu.cs464.p3.modulelang.linker.Linker;

import java.io.InputStream;
import java.io.InputStreamReader;

import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.Scanner;

/**
 * Generic entry point -- can be used for parsing an input stream, or 
 * for parsing and linking an inputstream. 
 *  	
 * @author zkieda
 */
public class ParseProgram {
	
    public static Program parse(InputStream in) throws Exception{
        ComplexSymbolFactory symbolFactory = new ComplexSymbolFactory();
        Scanner s = new Lexer(new InputStreamReader(in), symbolFactory);
        Parser p = new Parser(s, symbolFactory);
        return (Program)p.parse().value;
    }
    
    public static ClassTree parseAndLink(InputStream in) throws Exception{
    	return Linker.link(parse(in));
    }
    
//    public static void main(String args[]) throws Exception{
//    	String in = "import asd.in a.b.c a.b.n.* end asdf : asd : si : has a ab.f.g.g.u : has d e f:b:r.t:p3 g end c end";
//    	Program p = parse(new ByteArrayInputStream(in.getBytes()));
//    	System.out.println(p);
//    	System.out.println(ToString.genString(p));
//    }
}
