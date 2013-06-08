/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package speechrecognition.audio.features;

//import java.util.Arrays;
//import java.util.logging.Logger;
//import java.util.Map;
//import java.util.HashMap;
//import speechrecognition.util.ArrayUtil;
//import java.io.IOException;

import speechrecognition.audio.Clip;
import speechrecognition.audio.FrameSpectrum;
//import java.util.List;
//import java.util.ArrayList;

/**
 *
 * @author davinchi
 */
public class ClipPerFrameSpectrumFeatures extends ClipPerFrameFeatures {
    private int[] idx = null;
    
    public ClipPerFrameSpectrumFeatures(ClipPerFrameSpectrumFeatures other) {
        super(other);
    }

    public ClipPerFrameSpectrumFeatures(ClipPerFrameSpectrumFeatures other, int[] idx) {
        this(other);
        
        this.idx = idx;
    }

    public ClipPerFrameSpectrumFeatures(Clip clip, int framesN) {
        super(clip, framesN);
    }

    @Override
    public double[] getFeatureVector() {
        double[] features = new double[this.idx.length];
        assert (this.idx.length % framesN == 0);

        int freqPerFrame = this.idx.length / framesN;

        for (int i = 0, k = 0; i < framesN; ++i) {
            FrameSpectrum fs = getFrame(i);
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
    }
}