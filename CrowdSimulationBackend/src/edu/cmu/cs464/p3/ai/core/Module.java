package edu.cmu.cs464.p3.ai.core;

/**
 * @author zkieda
 */
public class Module implements GameUpdatable {
    private Player player;
    public final void init(Player player){
        this.player = player;
        init();
    }

    public Player getPlayer() {
        return player;
    }
    
    public void init(){}
    public void onFrameUpdate(){}
}
