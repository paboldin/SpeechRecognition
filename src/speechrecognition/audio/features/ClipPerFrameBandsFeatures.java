/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package speechrecognition.audio.features;

import java.util.Arrays;
import java.util.logging.Logger;
//import java.util.Map;
//import java.util.HashMap;
//import speechrecognition.util.ArrayUtil;
import java.io.IOException;

import speechrecognition.audio.Clip;
import speechrecognition.audio.FrameSpectrum;
//import java.util.List;
//import java.util.ArrayList;

/**
 *
 * @author davinchi
 */
public class ClipPerFrameBandsFeatures extends ClipPerFrameFeatures {

    private double[] bands = null;

    public ClipPerFrameBandsFeatures(ClipPerFrameBandsFeatures other) {
        super(other);
    }

    public ClipPerFrameBandsFeatures(ClipPerFrameBandsFeatures other, double[] bands) {
        this(other);

        this.bands = bands;
    }

    public ClipPerFrameBandsFeatures(Clip clip, int framesN) {
        super(clip, framesN);
    }

    @Override
    public double[] getFeatureVector() {
        int bandsN = this.bands.length - 1;
        double[] features = new double[bandsN * this.framesN];

        for (int i = 0; i < framesN; ++i) {
            FrameSpectrum fs = getFrame(i);

            for (int j = 0; j < bandsN; ++j) { // make this more generic?
                double minFreq = bands[j];
                double maxFreq = bands[j + 1];
                double bandPower = 0.0;
                int freq_idx = 0;

                while (freq_idx < freqs.length && freqs[freq_idx] < minFreq) {
                    ++freq_idx;
                }

                while (freq_idx < freqs.length && freqs[freq_idx] <= maxFreq) {
                    bandPower += Math.pow(fs.getReal(freq_idx), 2);
                    ++freq_idx;
                }

                features[i * bandsN + j] = Math.sqrt(bandPower) / (maxFreq - minFreq);
                //features[i * bandsN + j] = Math.sqrt(bandPower);
            }


            double maxBandPower = Double.NEGATIVE_INFINITY;
            for (int j = 0; j < bandsN; ++j) {
                maxBandPower = Math.max(features[i * bandsN + j], maxBandPower);
            }
            for (int j = 0; j < bandsN; ++j) {
                features[i * bandsN + j] /= maxBandPower;
            }
 
        }
/*
        double maxBandPower = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < features.length; ++i) {
            maxBandPower = Math.max(features[i], maxBandPower);
        }
        for (int i = 0; i < features.length; ++i) {
            features[i] /= maxBandPower;
        }
*/
        return features;

        /*        double[] features = new double[this.idx.length];
         assert (this.idx.length % framesN == 0);

         int freqPerFrame = this.idx.length / framesN;

         for (int i = 0, k = 0; i < framesN; ++i) {
         double maxSignal = Double.NEGATIVE_INFINITY;
         for (int j = 0; j < freqPerFrame; ++j) {
         maxSignal = Math.max(
         Math.abs(fs.getReal(idx[i * freqPerFrame + j])),
         maxSignal
         );
         }
         for (int j = 0; j < freqPerFrame; ++j) {
         features[k] = Math.abs(fs.getReal(idx[k])) / maxSignal;
         k++;
         }
         }

         return features;
         */
    }
}