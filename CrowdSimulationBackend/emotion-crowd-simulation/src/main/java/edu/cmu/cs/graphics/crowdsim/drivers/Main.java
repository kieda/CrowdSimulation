package edu.cmu.cs.graphics.crowdsim.drivers;

import edu.cmu.cs.graphics.crowdsim.ai.core.BasicInitialStateGenerator;
import edu.cmu.cs.graphics.crowdsim.ai.core.Game;
import edu.cmu.cs.graphics.crowdsim.module.ConstructModule;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


public class Main {
    public static void main(String[] args) throws Exception, ParseException {
    	InputStream gameModule = Main.class.getResourceAsStream("../Game.mod");
    	
    	Game game = ConstructModule.build(gameModule);
    	
    	//initialize modules which require external input
    	Optional<ArgsListenerModule> argsModule = game.getModuleByClass(ArgsListenerModule.class);
    	
    	if(argsModule.isPresent()) {
    		argsModule.get().init(args);
    		argsModule.get().processArgs();
    	}
    	
    	game.init(null); //initialize parent to null - kicks off start of all initialization.
    	game.run();
    }
}
