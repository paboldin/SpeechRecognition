/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package speechrecognition.audio;

/**
 *
 * @author davinchi
 */
public class Preemphasize {

    private final double preemphasisFactor;

    public Preemphasize(double preemph) {
        this.preemphasisFactor = preemph;
    }

    public void applyFilter(double[] in) {
        if (in.length > 1 && preemphasisFactor != 0.0) {
            // do preemphasis
            double current;
            double previous = in[0];
            for (int i = 1; i < in.length; i++) {
                current = in[i];
                in[i] = current - preemphasisFactor * previous;
                previous = current;
            }
        }
    }
}