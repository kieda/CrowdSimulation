package edu.cmu.cs464.p3.ai.core;

import edu.cmu.cs464.p3.ai.internal.InternalTraits;
import edu.cmu.cs464.p3.util.OpenSimplexNoise;
import edu.cmu.cs464.p3.util.PointToRealFn;
import java.awt.Dimension;
import java.util.Random;
import java.util.function.Function;
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
public class BasicInitialStateGenerator implements InitialStateGenerator{
    private final int redTeamPlayerCount;
    private final int blueTeamPlayerCount;
    private final Random gen = new Random();
    private final double boardWidth =  24.5;
    private final Bounds boardDimensions = new BoundingBox(-boardWidth, -boardWidth, boardWidth, boardWidth);
    private final PointToRealFn boardGenerator = new OpenSimplexNoise();

    
    public BasicInitialStateGenerator(int expectedTeamPlayerCount){
        this(expectedTeamPlayerCount, 0.5);
    }
    public BasicInitialStateGenerator(int expectedTeamPlayerCount, double precision){
        redTeamPlayerCount = (int) (gaussianInt(expectedTeamPlayerCount, precision));
        blueTeamPlayerCount = (int) (gaussianInt(expectedTeamPlayerCount, precision));
    }
    
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
    public void initialize(Function<String, Group> handle) {
        final Group red = handle.apply("red");
        final Group blue = handle.apply("blue");
        
        //counts for red team, blue team
        int redTeamCount = (int) (gaussianInt(expectedTeamPlayerCount, 0.5));
        int blueTeamCount = (int) (gaussianInt(expectedTeamPlayerCount, 0.5));
        
        
    }
}
