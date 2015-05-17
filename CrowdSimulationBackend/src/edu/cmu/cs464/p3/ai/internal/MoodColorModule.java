package edu.cmu.cs464.p3.ai.internal;

//todo make this a module 

import java.util.Arrays;
import javafx.scene.paint.Color;
import org.ejml.data.DenseMatrix32F;

// so we get the appropriate information
public class MoodColorModule{
    //represents our vector of colors
    // dimensions = {number of emptions, colors}
    // = 8x3 mat
    public DenseMatrix32F moodMat;
    private float[][] color(String... colorhex){
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