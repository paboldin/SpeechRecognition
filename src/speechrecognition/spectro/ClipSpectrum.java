/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package speechrecognition.spectro;

import java.util.Arrays;
import java.util.logging.Logger;
//import java.util.Map;
//import java.util.HashMap;
//import speechrecognition.util.ArrayUtil;
//import java.io.*;

/**
 *
 * @author davinchi
 */
public class ClipSpectrum {
    private static final Logger logger = Logger.getLogger(Frame.class.getName());

    
    
    private final double MAX_POWER_FRAC = 0.1;

    private double[] data;
    private double[] freqs;
    private String clipName;
    //private double min = Double.NEGATIVE_INFINITY, max = Double.POSITIVE_INFINITY;

    private ClipSpectrum(int size) {
        this.data = new double[size];
        this.freqs = new double[size];

        Arrays.fill(data, 0.0);
    }

    public ClipSpectrum(ClipSpectrum other) {
        this.data = new double[other.data.length];
        this.freqs = new double[other.freqs.length];
        this.clipName = other.clipName;
        
        System.arraycopy(other.freqs, 0, this.freqs, 0, this.freqs.length);
        System.arraycopy(other.data, 0, this.data, 0, this.data.length);
    }

    public ClipSpectrum(Clip clip) {
        this.data = new double[clip.getFrameFreqSamples()];
        this.freqs = new double[clip.getFrameFreqSamples()];
        this.clipName = clip.getName();

        Arrays.fill(data, 0.0);

        double fs = clip.getSamplingRate();

        for (int i = 0; i < this.data.length; ++i) {
            this.freqs[i] = i * fs / this.data.length / 2.;
        }
        
        copyFrames(clip);


        /*
         File out = new File("/home/davinchi/testme.txt");
         try {
         out.createNewFile();
         FileWriter fw = new FileWriter(out.getAbsoluteFile());
         BufferedWriter bw = new BufferedWriter(fw);
         PrintWriter pw = new PrintWriter(bw);
         pw.println(Arrays.toString(data));
         pw.println(Arrays.toString(freqs));
         } catch (IOException e) {
         }*/
    }
    
    private void copyFrames(Clip clip) {
        double maxPower = Double.NEGATIVE_INFINITY;
      
        for (Frame frame: clip.frames) {
            maxPower = Math.max(frame.power, maxPower);
        }
        
        for (Frame frame: clip.frames) {
            assert (frame.data.length == data.length);
            
            if (frame.power < maxPower * MAX_POWER_FRAC) {
                //logger.info("Discarding frame: " + frame.power + " max: " + maxPower);
                continue;
            }

            for (int i = 0; i < frame.data.length; ++i) {
                this.data[i] += Math.pow(frame.data[i], 2);
            }
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

    public void addSelf(ClipSpectrum other) {
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