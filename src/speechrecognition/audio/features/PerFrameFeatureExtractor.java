/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package speechrecognition.audio.features;

import java.util.Map;
import java.util.HashMap;
import speechrecognition.audio.Clip;
//import java.util.List;

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
