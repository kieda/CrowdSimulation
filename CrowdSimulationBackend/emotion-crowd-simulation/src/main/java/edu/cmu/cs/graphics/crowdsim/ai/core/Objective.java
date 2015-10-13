package edu.cmu.cs.graphics.crowdsim.ai.core;

/**
 * @author zkieda
 */
public interface Objective {
    public static enum ObjectiveStatus{
        FAIL, SUCCESS, RUNNING;
        public boolean hasEnded(){
            return this == FAIL || this == SUCCESS;
        }
    }
    
    PlayerObjective makePlayerObjective(Player p);
    ObjectiveStatus getObjectiveStatus();
}
