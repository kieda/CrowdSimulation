package edu.cmu.cs464.p3.modulelang;

import java_cup.runtime.Symbol;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.ComplexSymbolFactory.Location;

%%

%public
%class Lexer
%extends sym
// %unicode
%cup
%char
%line
%column

%{
    StringBuffer string = new StringBuffer();
    public Lexer(java.io.Reader in, ComplexSymbolFactory sf){
      this(in);
      symbolFactory = sf;
    }
    ComplexSymbolFactory symbolFactory;

    private Symbol symbol(String name, int sym) {
      return symbolFactory.newSymbol(name, sym, new Location(yyline+1,yycolumn+1), new Location(yyline+1,yycolumn+yylength()));
    }
    
    private Symbol symbol(String name, int sym, Object val) {
      Location left = new Location(yyline+1,yycolumn+1);
      Location right= new Location(yyline+1,yycolumn+yylength());
      return symbolFactory.newSymbol(name, sym, left, right,val);
    } 
    private Symbol symbol(String name, int sym, Object val,int buflength) {
      Location left = new Location(yyline+1,yycolumn+yylength()-buflength);
      Location right= new Location(yyline+1,yycolumn+yylength());
      return symbolFactory.newSymbol(name, sym, left, right,val);
    }       
    private void error(String message) {
      System.out.println("Error at line "+(yyline+1)+", column "+(yycolumn+1)+" : "+message);
    }
%} 

%eofval{
     return symbolFactory.newSymbol("EOF", EOF, new Location(yyline+1,yycolumn+1), new Location(yyline+1,yycolumn+1));
%eofval}


JavaToken = [a-zA-Z$_] [a-zA-Z0-9$_]*

NewLine = \r|\n|\r\n;

WhiteSpace = {NewLine} | [ \t\f]

/* %state STRING */

%%

<YYINITIAL>{
/* keywords */
"import"        { return symbol("import", IMPORT); }
"has"           { return symbol("has",HAS); }
"end"           { return symbol("end",END); }

/* names */
{JavaToken}           { return symbol("Identifier",IDENT, yytext()); }

/* separators */
":"               { return symbol(":", COLON); }
"."               { return symbol(".", DOT); }
"*"               { return symbol("*", STAR); }

{WhiteSpace}     { /* ignore */ }

}

/* error fallback */
.|\n  {   /* throw new Error("Illegal character <"+ yytext()+">");*/
          error("Illegal character <"+ yytext()+">");
      }