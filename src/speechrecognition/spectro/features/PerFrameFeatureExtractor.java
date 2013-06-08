/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package speechrecognition.spectro.features;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import speechrecognition.spectro.Clip;
import speechrecognition.spectro.FrameSpectrum;
//import java.util.List;
import speechrecognition.util.ArrayUtil;

/**
 *
 * @author davinchi
 *
 * TODO make this generic (on Feature) class?
 */
public abstract class PerFrameFeatureExtractor<T extends ClipPerFrameFeatures> 
                                extends FeaturesExtractor {

    protected final int framesCount;
    protected Map<Clip, T> clipToFeature = new HashMap<Clip, T>();

    public PerFrameFeatureExtractor(int framesCount) {
        this.framesCount = framesCount;
    }
    
/*    public abstract void featureFromClip(Clip clip);
    public abstract double[] extractFeatureVector(Clip clip);
    public abstract void before();
    public abstract void after();
    public abstract boolean alreadyExtracted();*/
}
