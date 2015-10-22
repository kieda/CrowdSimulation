package edu.cmu.cs.graphics.crowdsim.drivers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import edu.cmu.cs.graphics.crowdsim.module.AutoWired;
import edu.cmu.cs.graphics.crowdsim.module.ListenOn;
import edu.cmu.cs.graphics.crowdsim.module.SubModule;

/**
 * 
 * Represents a module that will be able to add or modify the command line arguments before
 * we process them. This will happen before the game actually starts, and we load the 
 * initial game environment.
 * 
 * This will allow certain game aspects to change the behavior of the command line arguments.
 * 
 * It is highly suggested that modules should NOT modify the command line arguments after they have
 * already been processed, such that the only modules that implement the ArgsListener class
 * are loaded with Game.mod.
 * 
 * Usage: 
 * 1. extend command line arguments using the accept method.
 * 2. initialize the command line arguments by running init(String[] args)
 * 3. conditions 1 and 2 must be satisfied. Run processArgs() to process 
 * the command line arguments.
 * 
 * @author zkieda
 */
public class ArgsListenerModule extends SubModule {
	public static interface ArgsModifier {
		public void modifyOptions(Options o);
		public void result(CommandLine cl);
	}
	
	// the options that we will use
	private final Options options = new Options();
	
	//autowired list of args modifiers
	//we'll process all of these after initialization
	private final @AutoWired Collection<ArgsModifier> modifiers = new ArrayList<>();
	private String[] args;
	
	public void init(String[] args){
		this.args = args;
	}
	
	//just parse args here... we should have init(args) already called.
	@Override
	public void init() {
		try {
			processArgs();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void processArgs() throws ParseException{
		modifiers.forEach(m -> {
			m.modifyOptions(options);
		});
		final CommandLineParser parser = new DefaultParser();
		final CommandLine result = parser.parse(options, args, true);
		modifiers.forEach(l -> l.result(result));
	}
}

