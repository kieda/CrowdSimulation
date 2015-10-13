package edu.cmu.cs.graphics.crowdsim.ai.core;

import edu.cmu.cs.graphics.crowdsim.util.Properties;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import javafx.geometry.Bounds;

/**
 * @author zkieda
 */
public interface InitialStateGenerator {
    //creates a series of configured groups that will play the game
    //using the handle
    
    //the handle is used to create and add groups players, and objects onto the
    //field. 
    
    //gameSetting is used to set the game's properties
    public void initialize(Function<Function<Group, Objective>, Function<String, Group>> handle,
            Properties gameSettings);
}
