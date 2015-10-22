package edu.cmu.cs.graphics.crowdsim.ai.core;

import edu.cmu.cs.graphics.crowdsim.module.AutoWired;
import edu.cmu.cs.graphics.crowdsim.module.MultiModule;
import edu.cmu.cs.graphics.crowdsim.serialize.SerializeGame;
import edu.cmu.cs.graphics.crowdsim.util.Properties;
import edu.cmu.cs.zkieda.quadtree.QuadTree;
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
 * represents the game that will be played. has rules built in.
 */
public class Game extends MultiModule implements Runnable {
	private AtomicInteger currentPlayerID = new AtomicInteger();
	private ArrayList<Group> groups;

	private List<GameUpdatable> gameUpdates;
	private QuadTree<GameObject> gameSpace;

	private @AutoWired SerializeGame gameSerialization;
	private @AutoWired NewFrameListenerModule onNewFrame;
	private @AutoWired InitialStateGenerator genFn;

	public QuadTree<GameObject>.QuadTreeImmutable getGameSpace() {
		return gameSpace.getImmutable();
	}

	private Game() {
		groups = new ArrayList<>();
		gameUpdates = new ArrayList<>();
	}

	@Override
	public void init() {
		genFn.init(this::genGroupFn);
		Bounds b = genFn.getGameBounds();
		gameSpace = new QuadTree<>(b.getMinX(), b.getMinY(), b.getMaxX(), b.getMaxY());
	}

	public Function<String, Group> genGroupFn(final Function<Group, Objective> objectiveFn) {
		return (str) -> {
			Group g = new Group(Game.this, str) {
				private final Objective obj = objectiveFn.apply(this);

				@Override
				public Objective getObjective() {
					return obj;
				}
			};
			groups.add(g);
			return g;
		};
	}

	public void addGameObject(GameObject go) {
		gameUpdates.add(go);
		final String id = go.getObjectID();
		go.initSerialize((key, val) -> gameSerialization.put(id, key, val));
		Vector2d pos = go.getState().getPositionProperty().get();
		gameSpace.put(pos, go);
		go.getState().getPositionProperty().addListener((evt, oldVal, newVal) -> {
			GameObject g = gameSpace.get(oldVal);
			gameSpace.put(newVal, g);
		});
	}

	private boolean isEndGame() {
		// check if all groups objectives are decidedly fulfilled or unfulfilled
		// note : objective status has ended if all players in group are dead
		return groups.stream().allMatch(g -> g.getObjective().getObjectiveStatus().hasEnded());
	}

	void addGameUpdatable(GameUpdatable gu) {
		gameUpdates.add(gu);
	}

	void remove(GameUpdatable gu) {
		gameUpdates.remove(gu);
	}

	@Override
	public void run() {
		while (!isEndGame()) step();
		
		try {
			gameSerialization.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// called until checkEndGame() returns false.
	private void step() {
		// update all game objects
		gameUpdates.forEach(GameUpdatable::onFrameUpdate);

		// at the end, run everything waiting for a frame update
		onNewFrame.onNewFrame();
	}

	int newPlayerID() {
		return currentPlayerID.incrementAndGet();
	}
}
