package edu.cmu.cs464.p3.ai.internal;

import edu.cmu.cs464.p3.ai.core.Player;
import edu.cmu.cs464.p3.ai.module.AutoWired;
import edu.cmu.cs464.p3.ai.module.SubModule;
import edu.cmu.cs464.p3.ai.perception.FRCPLevel;
import java.util.Arrays;
import org.ejml.simple.SimpleMatrix;

/**
 * @author zkieda
 */
public class TokenModule extends SubModule {
    private @AutoWired Player player;
    public static final int 
        COMMUNICATION = 0,
        CONFIDENCE = 1,
        COURAGE = 2, 
        INTELLIGENCE = 3, 
        PERCEPTION = 4;
    
    private int[] tokens;   //current number of tokens
    private int[] tokenCap; //current token cap
    private static int[] fromVector(SimpleMatrix mat){    
        int[] re = new int[mat.numRows()];
        Arrays.setAll(re, i -> (int)mat.get(i, 0));
        return re;
    }
    
    //TODO (currently simple translation)
    private final SimpleMatrix translationMat = new SimpleMatrix(
        new double[][]{
            {1, 0, 0, 0, 0},
            {0, 1, 0, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 0, 1, 0},
            {0, 0, 0, 0, 1}
        }).scale(10);
    
    @Override
    public void init() {
        //find token amounts based on the player's traits.
        tokens = new int[5];
        tokenCap = new int[5];
        
        //find and set the tokenCap
        tokenCap = fromVector(translationMat.mult(player.getTraits().getVector()));
    }
    
    @Override
    public void onFrameUpdate() {
        //reset token amounts
        Arrays.setAll(tokens, i -> tokenCap[i]);
        
        
        // don't think there's anything else
    }
    
    public synchronized double remaining(int tokenType){
        return (double)tokens[tokenType] / tokenCap[tokenType];
    }
    
    public synchronized int count(int tokenType){
        return tokens[tokenType];
    }
    
    public synchronized void spend(int tokenType, int numTokens){
        if(count(tokenType) < numTokens) 
            throw new IllegalArgumentException("Attempt to spend " + numTokens + " when only " + count(tokenType) + " are available.");
        tokens[tokenType] -= numTokens;
    }
    
    public FRCPLevel getRisk(int tokenType, int numTokens){
        return null; 
        // gives the frcp levels based on the token type we're spending 
        // and the number of tokens we're spending
    }
    
    //todo - method for calculating the risk (using internalState)
    //have tokens here as well 
    //tokens calculated from internaltraitss
    
    //?? make SubModule<InternalModule>
    // would give us info on the internal state, which can change risks
}
