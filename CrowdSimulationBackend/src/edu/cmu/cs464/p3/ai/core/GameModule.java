package edu.cmu.cs464.p3.ai.core;

/**
 * @author zkieda
 */
public class GameModule extends MultiModule<Player> {
     private int l;
    public void init(Game g){
        
    }
    public static class GroupModule extends SubModule<GameModule>{
    }
    public static class ObjectiveModule extends SubModule<GameModule>{
        private PlayerObjective objective;
        public void init(){
            this.objective = 
        }
    }
}
