package edu.cmu.cs464.p3.ai.core;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author zkieda
 */
public interface InitialStateGenerator {
    //creates a series of configured groups that will play the game
    //using the handle
    public void initialize(Function<Function<Group, Objective>, Function<String, Group>> handle);
}
