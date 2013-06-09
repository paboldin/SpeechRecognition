/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package speechrecognition.audio.features;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;


import speechrecognition.audio.Clip;


/**
 *
 * @author davinchi
 */
public abstract class FeaturesExtractor {
    public abstract void featureFromClip(Clip clip);
    public abstract double[] extractFeatureVector(Clip clip);
    public abstract void analyzeFeatureVector();
    
    public abstract boolean canSerialize();
    
    public abstract void serializeParameters(OutputStream os) throws IOException;
    
    public static FeaturesExtractor fromStream(BufferedReader br) throws IOException {
        throw new IllegalStateException("Not implemented in subclass");
    }
}
