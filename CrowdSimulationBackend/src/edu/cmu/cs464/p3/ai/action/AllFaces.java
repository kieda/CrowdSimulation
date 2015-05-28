package edu.cmu.cs464.p3.ai.action;

import edu.cmu.cs464.p3.ai.core.Face;
import edu.cmu.cs464.p3.ai.internal.MultiProbabilisticSubspace;
import edu.cmu.cs464.p3.ai.internal.ProbabilisticSubspace;

/**
 * @author zkieda
 */
public class AllFaces extends Face {
    private final String faceName;
    private final ProbabilisticSubspace properties;
    private static MultiProbabilisticSubspace<Face> faceSpace
            = new MultiProbabilisticSubspace<>();
    
    private AllFaces(String faceName, ProbabilisticSubspace properties) {
        this.faceName = faceName;
        this.properties = properties;
        faceSpace.insert(properties, this);
    }

    @Override
    public ProbabilisticSubspace getProperties() {
        return properties;
    }
    
    @Override
    public String getName(){
        return faceName;
    }
    
    public static MultiProbabilisticSubspace<Face> getFaceSpace(){
        return faceSpace;
    }
    
    public static final Face FACE_ANGRY = new AllFaces("angry", null);
    public static final Face FACE_ASHAMED = new AllFaces("ashamed", null);
    public static final Face FACE_CONFUSED = new AllFaces("confused", null);
    public static final Face FACE_DISCONCERTED = new AllFaces("disconcerted", null);
    public static final Face FACE_EXSTATIC = new AllFaces("exstatic", null);
    public static final Face FACE_FURIOUS = new AllFaces("furious", null);
    public static final Face FACE_HAPPY = new AllFaces("happy", null);
    public static final Face FACE_SAD = new AllFaces("sad", null);
    public static final Face FACE_SHOCKED = new AllFaces("shocked", null);
    public static final Face FACE_SICK = new AllFaces("sick", null);
    public static final Face FACE_SKEPTICAL = new AllFaces("skeptical", null);
}
