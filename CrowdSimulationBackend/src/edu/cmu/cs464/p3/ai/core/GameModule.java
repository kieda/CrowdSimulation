package edu.cmu.cs464.p3.ai.core;

import edu.cmu.cs464.p3.ai.module.SubModule;
import edu.cmu.cs464.p3.ai.module.MultiModule;
import edu.cmu.cs464.p3.ai.module.AutoWired;
import edu.cmu.cs464.p3.util.quadtree.QuadTree;

/**
 * @author zkieda
 */
public class GameModule extends MultiModule<Player> {
    private Game game;
    @AutoWired GroupModule groupModule;
    @AutoWired ObjectiveModule objectiveModule;
    
    public void init(Game g){
        this.game = g;
    }
    public static class GroupModule extends SubModule<GameModule>{
    }
    public static class ObjectiveModule extends SubModule<GameModule>{
        private @AutoWired Player player;
        private PlayerObjective objective;
        public void init(){
            this.objective = player.getObjective();
        }

        @Override
        public void onFrameUpdate() {
            //
        }
    }
    public QuadTree<GameObject>.QuadTreeImmutable getGameSpace(){
        return game.getGameSpace();
    }
}
