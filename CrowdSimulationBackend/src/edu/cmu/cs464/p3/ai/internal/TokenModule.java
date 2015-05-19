package edu.cmu.cs464.p3.ai.internal;

import edu.cmu.cs464.p3.ai.core.SubModule;
import edu.cmu.cs464.p3.ai.perception.FRCPLevel;
import java.util.Arrays;

/**
 * @author zkieda
 */
public class TokenModule extends SubModule {
    public static final int 
        COMMUNICATION = 0,
        CONFIDENCE = 1,
        COURAGE = 2, 
        INTELLIGENCE = 3, 
        PERCEPTION = 4;
    
    private int[] tokens;   //current number of tokens
    private int[] tokenCap; //current token cap
    
    @Override
    public void init() {
        //find token amounts based on the player's traits.
        tokens = new int[5];
        tokenCap = new int[5];

        //find and set the tokenCap
        tokenCap[COMMUNICATION] = getPlayer().getTraits().getCommunicationTokens();
        tokenCap[CONFIDENCE] = getPlayer().getTraits().getConfidenceTokens();
        tokenCap[COURAGE] = getPlayer().getTraits().getCourageTokens();
        tokenCap[INTELLIGENCE] = getPlayer().getTraits().getIntelligenceTokens();
        tokenCap[PERCEPTION] = getPlayer().getTraits().getPerceptionTokens();
        
        //todo find correct token amounts
    }
    
    @Override
    public void onFrameUpdate() {
        //reset token amounts
        Arrays.setAll(tokens, i -> tokenCap[i]);
        
        
        // don't think there's anything else
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
