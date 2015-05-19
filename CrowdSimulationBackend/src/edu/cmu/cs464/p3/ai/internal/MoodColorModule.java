package edu.cmu.cs464.p3.ai.internal;

//todo make this a module 

import edu.cmu.cs464.p3.ai.core.SubModule;
import edu.cmu.cs464.p3.util.MatrixUtil;
import java.util.Arrays;
import java.util.function.Consumer;
import javafx.scene.paint.Color;
import javax.vecmath.Color3f;
import org.apache.commons.math3.analysis.function.Gaussian;
import org.ejml.data.D1Matrix32F;
import org.ejml.data.DenseMatrix32F;
import org.ejml.ops.CommonOps;

// so we get the appropriate information
public class MoodColorModule extends SubModule<InternalModule>{
    //represents our vector of colors
    // dimensions = {number of emptions, colors}
    // = 8x3 mat
    public final DenseMatrix32F moodMat;
    private Gaussian[] gaussianUncertainties;
    private D1Matrix32F curColor;
    private D1Matrix32F curUncertainty;
    
    private static float[][] color(String... colorhex){
        float[][] res = new float[colorhex.length][3];
        Arrays.setAll(res, i -> {
            float[] f = new float[3];
            Color c = Color.web(colorhex[i]);
            f[0] = (float)c.getRed();
            f[1] = (float)c.getGreen();
            f[2] = (float)c.getBlue();
            return f;
        });
        return res;
    }
    public MoodColorModule(){
        moodMat = new DenseMatrix32F(color(
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
    private D1Matrix32F getExpectedPlayerColor(){
        return MatrixUtil.mult32(moodMat, getParent().getInternalState().getMoodSpace().getWeights());
    }
    
    private D1Matrix32F getExpectedPlayerUncertainty(){
        return MatrixUtil.mult32(moodMat, getParent().getInternalState().getMoodSpace().getUncertainties());
    }
    
    private Color3f toColor(D1Matrix32F vec){
        return new Color3f(vec.get(0, 0), vec.get(1, 0), vec.get(2, 0));
    }
    
    @Override
    public void init() {
        //set the initial mood color
        curColor = getExpectedPlayerColor();
        curUncertainty = getExpectedPlayerUncertainty();
        gaussianUncertainties = new Gaussian[InternalModule.NUM_MOODS];
        
        getParent().setMoodColor(toColor(curColor));
    }
    
    private D1Matrix32F getUncertainty(){
        
    }
    
    @Override
    public void onFrameUpdate() {
        //change the color of this player
        
        Color3f newColor = translate(getPlayer().getPlayerState().moodColorProperty().get(), 
                getParent().getInternalState().getMoodSpace());
        
        getParent().setMoodColor(newColor);
    }
    
    

    public Color3f translate(Color3f current, ProbabilisticSubspace emotion){
      //check if the current color is a good match for the emotional space. 
      //if the threshold value for changing is good enough, then we proceed to change colors
      
      //the emotion returns a probability space of colors.
      //we fit the current color inside of that probability space, and if we get an agreement level greater
      //than the agent's threshold, we proceed to change.

      //take the linear combination

      // (emotion.weights + gaussian of uncertainties) .  color 
      //return (emotion.getWeights().add(emotion.getUncertainties().makeGaussianVector())).dot(color);
      //something like this for calculating the new space.

        //todo : find the subspace for the color 
    }
}

// InternalModule : 
//   + getInternalState()
//   + getInternalTraits()
//   + getPlayer()
//   