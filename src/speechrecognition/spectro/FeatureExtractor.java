/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package speechrecognition.spectro;


/**
 *
 * @author davinchi
 */
public interface FeatureExtractor {
    public abstract void featureFromClip(Clip clip);
    public abstract double[] extractFeatureVector(Clip clip);
    public abstract void before();
    public abstract void after();
    
    public abstract boolean alreadyExtracted();
}
