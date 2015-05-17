package edu.cmu.cs464.p3.ai.core;

import edu.cmu.cs464.p3.serialize.SerializeGame;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * represents the game that will be played.
 * has rules built in.
 */
public class Game implements Runnable {
    private AtomicInteger currentPlayerID
        = new AtomicInteger();
    private ArrayList<Group> groups;
    private SerializeGame gameSerialization;
    private List<Runnable> newFrameListeners;
    private List<Consumer<Player.PlayerState>> newPlayerListeners;
    
    private Game(OutputStream out) {
        groups = new ArrayList<>();
        newFrameListeners = new ArrayList<>();
        newPlayerListeners = new ArrayList<>();
        
        gameSerialization = new SerializeGame(out, this::onNewFrameListener, this::onNewPlayerListener);
    }
    public static Game makeGame(OutputStream out, InitialStateGenerator genFn) {
        Game g = new Game(out);
        genFn.initialize(g::addGroup);
        return g;
    }
    private Group addGroup(String groupName){
        Group g = new Group(this, groupName);
        groups.add(g);
        return g;
    }
    private void onNewFrameListener(Runnable r){
        newFrameListeners.add(r);
    }
    private void onNewPlayerListener(Consumer<Player.PlayerState> p){
        newPlayerListeners.add(p);
    }
    
    private boolean isEndGame(){        
        //check if all groups objectives are decidedly fulfilled or unfulfilled
        //note : objective status has ended if all players in group are dead
        return groups.stream().allMatch(g -> g.getObjectiveStatus().hasEnded());
    }
    
    @Override
    public void run(){
        while(!isEndGame()) step();
    }
    //called until checkEndGame() returns false.
    private void step(){
        
        //at the end, run everything waiting for a frame update
        newFrameListeners.forEach(r -> r.run());
    }
    int newPlayerID(){
        return currentPlayerID.incrementAndGet();
    }
    
    void registerNewPlayer(Player p){
        newPlayerListeners.forEach(pl -> pl.accept(p.getPlayerState()));
    }
}
