#!/bin/bash
java -jar ../java-cup/java-cup-11b.jar -destdir src/edu/cmu/cs464/p3/modulelang/ -parser Parser src/lang/ModuleParser.cup
../jflex-1.6.1/jflex/bin/jflex -d src/edu/cmu/cs464/p3/modulelang/ src/lang/ModuleLexer.jflex