package edu.cmu.cs464.p3.ai.core;

import edu.cmu.cs464.p3.serialize.SerializeGame;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

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
    private List<GameUpdatable> gameUpdates;
    private List<Consumer<Player.PlayerState>> newPlayerListeners;
    
    private Game(OutputStream out) {
        groups = new ArrayList<>();
        gameUpdates = new ArrayList<>();
        newFrameListeners = new ArrayList<>();
        newPlayerListeners = new ArrayList<>();
        
        gameSerialization = new SerializeGame(out, this::onNewFrameListener, this::onNewPlayerListener);
    }
    public static Game makeGame(OutputStream out, InitialStateGenerator genFn) {
        Game g = new Game(out);
        genFn.initialize(g::genGroupFn);
        return g;
    }
    
    public Function<String, Group> genGroupFn(final Function<Group, Objective> objectiveFn){
        return (str) -> {
            Group g = new Group(Game.this, str) {
                private final Objective obj = objectiveFn.apply (this);
                @Override
                public Objective getObjective() {
                    return obj;
                }
            };
            groups.add(g);
            return g;
        };
    }
    void addFlag(Flag f){
        gameSerialization.addFlagState(f.getFlagState());
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
        return groups.stream().allMatch(g -> g.getObjective().getObjectiveStatus().hasEnded());
    }
    void addGameUpdatable(GameUpdatable gu){
        gameUpdates.add(gu);
    }
    void removeGameUpdatable(GameUpdatable gu){
        gameUpdates.remove(gu);
    }
    @Override
    public void run(){
        while(!isEndGame()) step();
    }
    //called until checkEndGame() returns false.
    private void step(){
        //update all game objects
        gameUpdates.forEach(GameUpdatable::onFrameUpdate);
        
        //at the end, run everything waiting for a frame update
        newFrameListeners.forEach(r -> r.run());
    }
    int newPlayerID(){
        return currentPlayerID.incrementAndGet();
    }
    
    void registerNewPlayer(Player p){
        gameUpdates.add(p);
        newPlayerListeners.forEach(pl -> pl.accept(p.getPlayerState()));
    }
}
