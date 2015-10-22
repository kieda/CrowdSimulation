package edu.cmu.cs.graphics.crowdsim.ai.internal;

import edu.cmu.cs.graphics.crowdsim.ai.perception.PerceptionModule;
import edu.cmu.cs.graphics.crowdsim.module.AutoWired;
import edu.cmu.cs.graphics.crowdsim.module.MultiModule;

import java.util.function.Consumer;
import javax.vecmath.Color3f;

//represents a model that affects the internal behavior.
public class InternalModule extends MultiModule{
    public static final int NUM_MOODS = 8;
    
    private @AutoWired EmotionLinearSystem internalState;
    private @AutoWired PerceptionModule perception;
    private @AutoWired MoodColorModule moodColor;
    
    private Consumer<Color3f> changeColorFn;
    
    public void setMoodColor(Color3f color){
        changeColorFn.accept(color);
    }
    
    public void init(Consumer<Color3f> changeColorFn){
        this.changeColorFn = changeColorFn;
        
//        internalState = new EmotionLinearSystem();
//        internalState.addModule(new BasicEmotionGraphConstructor());
//        
//        moodColor = new MoodColorModule();
//        
//        addModule(internalState);
//        addModule(moodColor);
    }
}