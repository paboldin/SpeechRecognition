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
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author davinchi
 */
public class ClipPerFrameSpectrumFeature implements ClipFeature {

    private static final Logger logger = Logger.getLogger(FrameSpectrum.class.getName());
    private final double MAX_POWER_FRAC = 0.1;
    private int framesN;
    private FrameSpectrum[] framesSpectrum;
    private double[] freqs;
    private int[] idx = null;
    private String clipName;

    private void initFreqs(int freqs, double fs) {
        this.freqs = new double[freqs];

        Arrays.fill(this.freqs, 0.0);

        for (int i = 0; i < this.freqs.length; ++i) {
            this.freqs[i] = i * fs / this.freqs.length / 2.;
        }
    }

    private ClipPerFrameSpectrumFeature(int framesN) {
        this.framesN = framesN;
        this.framesSpectrum = new FrameSpectrum[framesN]; //FrameSpectrum[this.framesN];
    }

    public ClipPerFrameSpectrumFeature(ClipPerFrameSpectrumFeature other, int[] idx) {
        this(other);
        this.idx = idx;
    }

    public ClipPerFrameSpectrumFeature(ClipPerFrameSpectrumFeature other) {
        this(other.framesN);

        this.clipName = other.clipName;

        for (int i = 0; i < framesN; ++i) {
            this.framesSpectrum[i] = new FrameSpectrum(other.framesSpectrum[i]);
        }
    }

    public ClipPerFrameSpectrumFeature(Clip clip, int framesN) {
        this(framesN);

        this.clipName = clip.getName();

        initFreqs(clip.getFrameFreqSamples(), clip.getSamplingRate());

        copyFrames(clip);
    }

    public int getFramesCount() {
        return framesN;
    }

    public FrameSpectrum getFrame(int i) {
        return framesSpectrum[i];
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

    private void copyFrames(Clip clip) {
        double maxPower = Double.NEGATIVE_INFINITY;

        try {

            for (int i = 0; i < clip.getFrameCount(); ++i) {
                FrameSpectrum frame = clip.getFrame(i);
                maxPower = Math.max(frame.power, maxPower);
            }

            int startI = -1, stopI = -1;

            for (int i = 0; i < clip.getFrameCount(); ++i) {
                FrameSpectrum frame = clip.getFrame(i);

                if (startI == -1) {
                    if (frame.power < maxPower * MAX_POWER_FRAC) {
                        continue;
                    }
                    startI = i;
                }

                if (stopI == -1) {
                    if (frame.power < maxPower * MAX_POWER_FRAC) {
                        stopI = i;
                        break;
                    }
                }
            }

            if (stopI == -1) {
                stopI = clip.getFrameCount() - 1;
            }


//            System.out.println(startI + " " + stopI);

            final int framesSelected = stopI - startI;
            final double step = (double) (framesSelected - 1) / (double) (framesN - 1);
            for (int i = 0; i < framesN; ++i) {
                framesSpectrum[i] = clip.getFrame((int) Math.round(step * i));
            }
        } catch (IOException e) {
        } finally {
            clip.unloadFrames();
        }
    }

    //@Override
    public void addSelf(ClipPerFrameSpectrumFeature other) {
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

    public double[] getFreqs() {
        return freqs;
    }
}