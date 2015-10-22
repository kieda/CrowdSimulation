package edu.cmu.cs.graphics.crowdsim.ai.core;

import edu.cmu.cs.graphics.crowdsim.module.AutoWired;
import edu.cmu.cs.graphics.crowdsim.module.MultiModule;
import edu.cmu.cs.graphics.crowdsim.module.SubModule;
import edu.cmu.cs.zkieda.quadtree.QuadTree;

/**
 * @author zkieda
 */
public class GameModule extends MultiModule {
    private Game game;
    @AutoWired GroupModule groupModule;
    @AutoWired ObjectiveModule objectiveModule;
    
    public void init(Game g){
        this.game = g;
    }
    public static class GroupModule extends SubModule {
    }
    public static class ObjectiveModule extends SubModule {
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
