package edu.cmu.cs464.p3.modulelang.parser;

import edu.cmu.cs464.p3.modulelang.ast.Program;
import edu.cmu.cs464.p3.modulelang.linker.NTree;
import edu.cmu.cs464.p3.modulelang.linker.Linker;
import edu.cmu.cs464.p3.modulelang.parser.Lexer;
import edu.cmu.cs464.p3.modulelang.parser.Parser;

import java.io.ByteArrayInputStream;
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
    
    public static NTree<Class<?>> parseAndLink(InputStream in) throws Exception{
    	return Linker.link(parse(in));
    }
    
//    public static void main(String[] args) throws Exception{
//    	String s = "import java.util.List java.lang.* scala.collection.* java.util.Map end Map : List : Boolean : has Integer Character java.util.ArrayList end";
//    	System.out.println(parseAndLink(new ByteArrayInputStream(s.getBytes())));
//    }
}
