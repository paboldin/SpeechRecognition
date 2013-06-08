/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package speechrecognition.spectro.features;

import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;


import speechrecognition.spectro.Clip;


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
    
    public static FeaturesExtractor fromStream(InputStream is) throws IOException {
        throw new IllegalStateException("Not implemented in subclass");
    }
}
