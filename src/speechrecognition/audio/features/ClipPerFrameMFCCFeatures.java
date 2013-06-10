/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package speechrecognition.audio.features;

import java.util.Arrays;
import speechrecognition.audio.Clip;
import speechrecognition.audio.FrameSpectrum;
import speechrecognition.audio.features.mfcc.FFT;
import speechrecognition.audio.features.mfcc.MFCC;
//import java.util.List;
//import java.util.ArrayList;

/**
 *
 * @author davinchi
 */
public class ClipPerFrameMFCCFeatures extends ClipPerFrameFeatures {

    private int mfccCount = 13;
    private int mfccMelBands = 40;

    public ClipPerFrameMFCCFeatures(ClipPerFrameMFCCFeatures other) {
        super(other);
    }

    public ClipPerFrameMFCCFeatures(ClipPerFrameMFCCFeatures other, int mfccCount, int mfccMelBands) {
        this(other);

        this.mfccCount = mfccCount;
        this.mfccMelBands = mfccMelBands;
    }

    public ClipPerFrameMFCCFeatures(Clip clip, int framesN) {
        super(clip, framesN);
    }

    @Override
    public double[] getFeatureVector() {
        double[] features = new double[mfccCount * this.framesN];

        MFCC mfcc = null; //new MFCC(mfccCount);
        FFT fft = null;

        double maxMFCC = Double.NEGATIVE_INFINITY;
        double minMFCC = Double.POSITIVE_INFINITY;
        
        for (int i = 0; i < framesN; ++i) {
            FrameSpectrum fs = getFrame(i);

            if (mfcc == null) {
                mfcc = new MFCC(fs.getLength(), mfccCount, mfccMelBands, sampleRate);
            }
            if (fft == null) {
                fft = new FFT(fs.getLength());
            }
            
            double re[] = new double[fs.timeData.length];
            double im[] = new double[fs.timeData.length];
            
            System.arraycopy(fs.timeData, 0, re, 0, fs.timeData.length);
            Arrays.fill(im, 0);
            
            fft.fft(re, im);

            double data[] = mfcc.cepstrum(re, im);
            for (int j = 0; j < data.length; ++j) {
                features[i * mfccCount + j] = data[j];

                maxMFCC = Math.max(data[j], maxMFCC);
                minMFCC = Math.min(data[j], minMFCC);
            }

        }

        for (int j = 0; j < features.length; ++j) {
            features[j] -= minMFCC;
            features[j] /= (maxMFCC - minMFCC);
        }

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