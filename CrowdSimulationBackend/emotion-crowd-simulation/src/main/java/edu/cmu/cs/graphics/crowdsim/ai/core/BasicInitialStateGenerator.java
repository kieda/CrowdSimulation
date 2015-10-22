package edu.cmu.cs.graphics.crowdsim.ai.core;

import edu.cmu.cs.graphics.crowdsim.ai.internal.InternalTraits;
import edu.cmu.cs.graphics.crowdsim.drivers.ArgsListenerModule.ArgsModifier;
import edu.cmu.cs.graphics.crowdsim.module.MultiModule;
import edu.cmu.cs.graphics.crowdsim.util.OpenSimplexNoise;
import edu.cmu.cs.graphics.crowdsim.util.PointToRealFn;
import edu.cmu.cs.graphics.crowdsim.util.Properties;
import java.awt.Dimension;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Dimension2D;

/**
 * generates a team, their location, and where they look towards
 * locations based on their interest perception.
 * 
 * team emotions and traits are gaussian distributed, along with player count
 * for each team
 * 
 * team locations based on simplex noise, only located on opposing sides
 * place down individuals based on expected comfort level
 * gauge comfort based on probability an enemy or teammate will be nearby
 * when game starts
 *
 * finally, we use the perception API to determine individuals direction
 * @author zkieda
 */
public class BasicInitialStateGenerator extends MultiModule implements InitialStateGenerator, ArgsModifier{
    private int redTeamPlayerCount;
    private int blueTeamPlayerCount;
    private final Random gen = new Random();
    private final double boardWidth =  24.5;
    private final Bounds boardDimensions = new BoundingBox(-boardWidth, -boardWidth, 2*boardWidth, 2*boardWidth);
    private final PointToRealFn boardGenerator = new OpenSimplexNoise();

    
//    public BasicInitialStateGenerator(int expectedTeamPlayerCount){
//        this(expectedTeamPlayerCount, 0.5);
//    }
//    public BasicInitialStateGenerator(int expectedTeamPlayerCount, double precision){
//        redTeamPlayerCount = (int) (gaussianInt(expectedTeamPlayerCount, precision));
//        blueTeamPlayerCount = (int) (gaussianInt(expectedTeamPlayerCount, precision));
//    }
    
    private int gaussianInt(int other, double percision){
        return (int) ((1.0 - percision) * other * (gen.nextGaussian() + 1.0) 
                + percision * other);
    }
    
    private InternalTraits genTraits() {
        return new InternalTraits(
            gen.nextGaussian(),
            gen.nextGaussian(),
            gen.nextGaussian(),
            gen.nextGaussian(),
            gen.nextGaussian());
    }
    
    @Override
    public void init(Function<Function<Group, Objective>, Function<String, Group>> handle, 
            Properties gameSettings) {
        final Function<String, Group> ctfFactory = handle.apply(CaptureTheFlag::new);
        final Group red = ctfFactory.apply("red");
        final Group blue = ctfFactory.apply("blue");
        
        //todo - 1. add in players
        // 2. add in flags
        
        gameSettings.put(Game.PROPERTY_GAME_BOUNDS, boardDimensions);
        
    }
    
    private static final String PLAYERS = "players";
    private static final String PRECISION = "precision";
    
	@Override
	public void modifyOptions(Options o) {
		o.addOption(new Option(PRECISION, false, 
              "the precision of the number of players on each team"));
		o.addOption(new Option(PLAYERS, false, 
              "the expected number of players on each team"));
	}
	@Override
	public void result(CommandLine cl) {
		int numPlayers = cl.hasOption(PLAYERS) ? Integer.parseInt(cl.getOptionValue(PLAYERS)) : 10;
		double precision = cl.hasOption(PRECISION) ? Double.parseDouble(cl.getOptionValue(PRECISION)) : 0.5;
		
		redTeamPlayerCount = (int) (gaussianInt(numPlayers, precision));
        blueTeamPlayerCount = (int) (gaussianInt(numPlayers, precision));
	}
}
