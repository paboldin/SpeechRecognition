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

/**
 *
 * @author davinchi
 */
public class ClipSpectrumFeature implements ClipFeatures {
    private static final Logger logger = Logger.getLogger(FrameSpectrum.class.getName());

    
    
    private final double MAX_POWER_FRAC = 0.1;

    private double[] data;
    private double[] freqs;
    private int[] idx = null;
    private String clipName;
    //private double min = Double.NEGATIVE_INFINITY, max = Double.POSITIVE_INFINITY;

    private ClipSpectrumFeature(int size) {
        this.data = new double[size];
        this.freqs = new double[size];

        Arrays.fill(data, 0.0);
    }
    
    public ClipSpectrumFeature(ClipSpectrumFeature other, int[] idx) {
        this.data = new double[other.data.length];
        this.freqs = new double[other.freqs.length];
        this.clipName = other.clipName;
        this.idx = idx;
        
        System.arraycopy(other.freqs, 0, this.freqs, 0, this.freqs.length);
        System.arraycopy(other.data, 0, this.data, 0, this.data.length);
    }

    
    public ClipSpectrumFeature(ClipSpectrumFeature other) {
        this.data = new double[other.data.length];
        this.freqs = new double[other.freqs.length];
        this.clipName = other.clipName;
        
        System.arraycopy(other.freqs, 0, this.freqs, 0, this.freqs.length);
        System.arraycopy(other.data, 0, this.data, 0, this.data.length);
    }

    public ClipSpectrumFeature(Clip clip) {
        this.data = new double[clip.getFrameFreqSamples()];
        this.freqs = new double[clip.getFrameFreqSamples()];
        this.clipName = clip.getName();

        Arrays.fill(data, 0.0);

        double fs = clip.getSamplingRate();

        for (int i = 0; i < this.data.length; ++i) {
            this.freqs[i] = i * fs / this.data.length / 2.;
        }
        
        copyFrames(clip);
    }
    
    @Override
    public double[] getFeatureVector() {
        double[] features = new double[this.idx.length];
        
        for (int i = 0; i < this.idx.length; ++i)
            features[i] = this.data[idx[i]];
        
        return features;
    }
    
    private void copyFrames(Clip clip) {
        double maxPower = Double.NEGATIVE_INFINITY;
      
        try {
        
            for (int i = 0; i < clip.getFrameCount(); ++i) {
                FrameSpectrum frame = clip.getFrame(i);
                maxPower = Math.max(frame.power, maxPower);
            }

            for (int i = 0; i < clip.getFrameCount(); ++i) {
                FrameSpectrum frame = clip.getFrame(i);

                assert (frame.data.length == data.length);

                if (frame.power < maxPower * MAX_POWER_FRAC) {
                    //logger.info("Discarding frame: " + frame.power + " max: " + maxPower);
                    continue;
                }


                for (int j = 0; j < frame.getLength(); ++j) {
                    this.data[j] += Math.pow(frame.getReal(j), 2);
                }
            }

        } catch (IOException e) {
        } finally {
            clip.unloadFrames();
        }

        for(int i = 0; i < data.length; ++i)
            data[i] = Math.sqrt(data[i]);

        this.normalizeData();
    }

    private void normalizeData() {
        double max = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < this.data.length; ++i) {
            max = Math.max(this.data[i], max);
        }

        for (int i = 0; i < this.data.length; ++i) {
            this.data[i] /= max;
        }
    }

    //@Override
    public void addSelf(ClipSpectrumFeature other) {
        //double freqs[] = other.getFreqs();
        //double data[] = other.getData();
        assert(Arrays.equals(this.freqs, other.freqs));
        // This is not full.
        assert (this.data.length == other.data.length);

        for (int i = 0; i < this.data.length; ++i) {
            this.data[i] += other.data[i];
        }
        
        this.clipName += ';' + other.clipName;
    }

    public double[] getData() {
        return data;
    }

    public double[] getFreqs() {
        return freqs;
    }
}