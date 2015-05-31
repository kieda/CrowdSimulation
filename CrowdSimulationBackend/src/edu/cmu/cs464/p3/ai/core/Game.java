package edu.cmu.cs464.p3.ai.core;

import edu.cmu.cs464.p3.serialize.SerializeGame;
import edu.cmu.cs464.p3.util.Properties;
import edu.cmu.cs464.p3.util.quadtree.QuadTree;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import javafx.geometry.Bounds;
import javax.vecmath.Vector2d;
        
/**
 * represents the game that will be played.
 * has rules built in.
 */
public class Game implements Runnable {
    private AtomicInteger currentPlayerID = new AtomicInteger();
    private ArrayList<Group> groups;
    private SerializeGame gameSerialization;
    private List<Runnable> newFrameListeners;
    private List<GameUpdatable> gameUpdates;
    private QuadTree<GameObject> gameSpace;
    
    public static final String PROPERTY_GAME_BOUNDS = "GameBounds";
    
    public QuadTree<GameObject>.QuadTreeImmutable getGameSpace(){
        return gameSpace.getImmutable();
    }
    
    private Game(OutputStream out) {
        // todo - some way for the initial state to tell the
        // gameSpace the size of the field.
        groups = new ArrayList<>();
        gameUpdates = new ArrayList<>();
        
        newFrameListeners = new ArrayList<>();
        
        gameSerialization = new SerializeGame(out, this::onNewFrameListener);
    }


    //construct from the game settings
    private void build(Properties gs){
        Bounds b = gs.get(PROPERTY_GAME_BOUNDS);
        gameSpace = new QuadTree<>(b.getMinX(), b.getMinY(), b.getMaxX(), b.getMaxY());
    }
    
    
    public static Game makeGame(OutputStream out, InitialStateGenerator genFn) {
        Game g = new Game(out);
        Properties gs = new Properties();
        genFn.initialize(g::genGroupFn, gs);
        g.build(gs);
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
    
    public void addGameObject(GameObject go){
        gameUpdates.add(go);
        final String id = go.getObjectID();
        go.initSerialize((key, val)-> gameSerialization.put(id, key, val));
        Vector2d pos = go.getState().getPositionProperty().get();
        gameSpace.put(pos, go);
        go.getState().getPositionProperty().addListener((evt, oldVal, newVal) -> {
            GameObject g = gameSpace.get(oldVal);
            gameSpace.put(newVal, g);
        });
    }

    private void onNewFrameListener(Runnable r){
        newFrameListeners.add(r);
    }

    private boolean isEndGame(){        
        //check if all groups objectives are decidedly fulfilled or unfulfilled
        //note : objective status has ended if all players in group are dead
        return groups.stream().allMatch(g -> g.getObjective().getObjectiveStatus().hasEnded());
    }
    void addGameUpdatable(GameUpdatable gu){
        gameUpdates.add(gu);
    }
    void remove(GameUpdatable gu){
        gameUpdates.remove(gu);
    }
    @Override
    public void run(){
        while(!isEndGame()) step();
        try{
            gameSerialization.close();
        }catch(IOException e){}
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
}
