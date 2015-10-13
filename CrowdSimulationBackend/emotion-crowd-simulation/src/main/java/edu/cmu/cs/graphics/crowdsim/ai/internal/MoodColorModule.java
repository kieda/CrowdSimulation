package edu.cmu.cs.graphics.crowdsim.ai.internal;

//todo make this a module 

import edu.cmu.cs.graphics.crowdsim.ai.module.SubModule;
import java.util.Arrays;
import javafx.scene.paint.Color;
import javax.vecmath.Color3f;
import org.ejml.ops.NormOps;
import org.ejml.simple.SimpleMatrix;

// so we get the appropriate information
public class MoodColorModule extends SubModule<InternalModule>{
    //represents our vector of colors
    // dimensions = {number of emptions, colors}
    // = 8x3 mat
    public final SimpleMatrix moodMat;
    private ProbabilisticSubspace curColorSpace;
    
    private static double[][] color(String... colorhex){
        double[][] res = new double[colorhex.length][3];
        Arrays.setAll(res, i -> {
            double[] f = new double[3];
            Color c = Color.web(colorhex[i]);
            f[0] = c.getRed();
            f[1] = c.getGreen();
            f[2] = c.getBlue();
            return f;
        });
        return res;
    }
    public MoodColorModule(){
        moodMat = new SimpleMatrix(color(
            "ffff54", // happiness
            "54ff54", // trust 
            "009600", // fear
            "59bdff", // surprise 
            "5151ff", // sadness
            "ff54ff", // disgust
            "ff0000", // anger
            "ffa854" // anticipation
        ));
        
        //emotion over value 0.5 will increase in saturation, decrease in 
        //value
    }
    private ProbabilisticSubspace getExpectedPlayerColor(){
        return getParent().getInternalState().getMoodSpace().transform(moodMat);
    }
    
    
    private Color3f toColor(SimpleMatrix vec){
        return new Color3f(
                (float)vec.get(0, 0), 
                (float)vec.get(1, 0), 
                (float)vec.get(2, 0));
    }
    
    @Override
    public void init() {
        //set the initial mood color
        curColorSpace = getExpectedPlayerColor();
        getParent().setMoodColor(toColor(curColorSpace.getWeights()));
    }
    /**
     * TODO 
     * 
     * returns the threshold difference change we should have before changing
     * emotions. Changes linearly correlate to the current emotions.
     * @return 
     */
    private double thresholdChange(){
        return 0.0;
    }
    
    @Override
    public void onFrameUpdate() {
        //change the color of this player
        //check if the current color is a good match for the emotional space. 
        //if the threshold value for changing is good enough, then we proceed to change colors

        //the emotion returns a probability space of colors.
        //we fit the current color inside of that probability space, and if we get an agreement level greater
        //than the agent's threshold, we proceed to change.

        //take the linear combination

        // (emotion.weights + gaussian of uncertainties) .  color 
        //return (emotion.getWeights().add(emotion.getUncertainties().makeGaussianVector())).dot(color);
        //something like this for calculating the new space.
                
        ProbabilisticSubspace targetColorSpace = getExpectedPlayerColor();
        double diff = NormOps.fastNormF(curColorSpace.probabilityDiff(targetColorSpace.getWeights()).getMatrix());
        if(diff >= thresholdChange()){
            getParent().setMoodColor(toColor(targetColorSpace.getRandomValue()));
        }
    }

}

// InternalModule : 
//   + getInternalState()
//   + getInternalTraits()
//   + getPlayer()
//   