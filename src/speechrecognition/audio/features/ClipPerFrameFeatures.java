/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package speechrecognition.audio.features;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

import speechrecognition.audio.Clip;
import speechrecognition.audio.FrameSpectrum;

/**
 *
 * @author davinchi
 */
public abstract class ClipPerFrameFeatures implements ClipFeatures {
    protected static final Logger logger = Logger.getLogger(FrameSpectrum.class.getName());
    protected String clipName;
    protected int framesN;
    protected FrameSpectrum[] framesSpectrum;
    protected double[] freqs;
    protected final double MAX_POWER_FRAC = 0.1;
    protected double sampleRate;
   
    
    protected ClipPerFrameFeatures(int framesN) {
        this.framesN = framesN;
        this.framesSpectrum = new FrameSpectrum[framesN]; //FrameSpectrum[this.framesN];
    }
    
    protected ClipPerFrameFeatures(Clip clip, int framesN) {
        this(framesN);
        
        this.sampleRate = clip.getSamplingRate();
        this.clipName = clip.getName();

        initFreqs(clip.getFrameFreqSamples(), clip.getSamplingRate());

        copyFrames(clip);
    }

    public ClipPerFrameFeatures(ClipPerFrameFeatures other) {
        this(other.framesN);

        this.clipName = other.clipName;
        this.freqs = other.freqs;
        this.sampleRate = other.sampleRate;

        for (int i = 0; i < framesN; ++i) {
            this.framesSpectrum[i] = new FrameSpectrum(other.framesSpectrum[i]);
        }
    }

    public FrameSpectrum getFrame(int i) {
        return framesSpectrum[i];
    }

    public int getFramesCount() {
        return framesN;
    }

    public double[] getFreqs() {
        return freqs;
    }

    private void initFreqs(int freqs, double fs) {
        this.freqs = new double[freqs];
        Arrays.fill(this.freqs, 0.0);
        for (int i = 0; i < this.freqs.length; ++i) {
            this.freqs[i] = i * fs / this.freqs.length / 2.;
        }
    }

    private void copyFrames(Clip clip) {
        double maxPower = Double.NEGATIVE_INFINITY;
        try {
            int maxIndx = 0;
            for (int i = 0; i < clip.getFrameCount(); ++i) {
                FrameSpectrum frame = clip.getFrame(i);
                if (frame.power > maxPower) {
                    maxPower = frame.power;
                    maxIndx = i;
                }
            }
            
            int startI = 0;
            int stopI = clip.getFrameCount();
            for (int i = maxIndx - 1; i >= 0; --i) {
                FrameSpectrum frame = clip.getFrame(i);

                if (frame.power < maxPower * MAX_POWER_FRAC) {
                    startI = i + 1;
                    break;
                }
            }
            
            for (int i = maxIndx + 1; i < clip.getFrameCount(); ++i) {
                FrameSpectrum frame = clip.getFrame(i);
                if (frame.power < maxPower * MAX_POWER_FRAC) {
                    stopI = i - 1;
                    break;
                }
            }
            
            //            System.out.println(startI + " " + stopI);
            final int framesSelected = stopI - startI;
            final double step = (double) (framesSelected - 1) / (double) (framesN - 1);
            System.out.println("step = " + step);
            for (int i = 0; i < framesN; ++i) {
                framesSpectrum[i] = clip.getFrame(startI + (int) Math.round(step * i));
            }
        } catch (IOException e) {
            System.err.println(e);
        } finally {
            clip.unloadFrames();
        }
    }

    //@Override
    public void addSelf(ClipPerFrameSpectrumFeatures other) {
        //double freqs[] = other.getFreqs();
        //double data[] = other.getData();
        assert (Arrays.equals(this.freqs, other.freqs));
        // This is not full.
        assert (this.framesN == other.framesN);
        for (int i = 0; i < this.framesN; ++i) {
            FrameSpectrum fs = framesSpectrum[i];
            fs.addSelf(other.framesSpectrum[i]);
        }
        this.clipName += ';' + other.clipName;
    }
    
}
