package edu.cmu.cs464.p3.ai.core;

import javax.vecmath.Color3f;
import javax.vecmath.Vector2d;

/**
 * @author zkieda
 */
public class PlayerActions {
    private final Player.PlayerState playerState;
    PlayerActions(Player.PlayerState playerState){
        this.playerState = playerState;
    }
    public void setPosition(Vector2d pos){
        playerState.position.set(pos);
    }
    
    public void setDirection(Vector2d dir){
        playerState.direction.set(dir);
    }
    
    public void destroy(){
        playerState.isDead.set(true);
    }
    
    public void setAttacking(boolean isAttacking){
        playerState.isAttacking.set(isAttacking);
    }
    
    public void setMoodColor(Color3f moodColor){
        playerState.moodColor.set(moodColor);
    }
}
