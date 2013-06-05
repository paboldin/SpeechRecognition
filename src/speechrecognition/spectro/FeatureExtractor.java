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
    public void featureFromClip(Clip clip);
    public double[] extractFeatureVector(Clip clip);
    public void before();
    public void after();
}
