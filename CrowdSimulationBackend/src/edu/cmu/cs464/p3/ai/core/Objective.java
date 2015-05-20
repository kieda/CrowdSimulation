package edu.cmu.cs464.p3.ai.core;

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
