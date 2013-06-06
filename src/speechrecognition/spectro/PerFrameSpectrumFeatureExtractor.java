/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package speechrecognition.spectro;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
//import java.util.List;
import speechrecognition.util.ArrayUtil;

/**
 *
 * @author davinchi
 *
 * TODO make this generic (on Feature) class?
 */
public class PerFrameSpectrumFeatureExtractor implements FeatureExtractor {

    private final int freqsCount;
    private final int framesCount;
    private int[] featureVectorIdx;
    private Map<Clip, ClipPerFrameSpectrumFeature> clipToFeature =
            new HashMap<Clip, ClipPerFrameSpectrumFeature>();

    public PerFrameSpectrumFeatureExtractor(int freqsCount, int framesCount) {
        this.framesCount = framesCount;
        this.freqsCount = freqsCount;
        this.featureVectorIdx = null;
    }

    @Override
    public void featureFromClip(Clip clip) {
        ClipPerFrameSpectrumFeature csf = new ClipPerFrameSpectrumFeature(
                clip, framesCount);
        clipToFeature.put(clip, csf);
    }

    @Override
    public double[] extractFeatureVector(Clip clip) {
        ClipPerFrameSpectrumFeature csf = new ClipPerFrameSpectrumFeature(
                clipToFeature.get(clip),
                this.featureVectorIdx
        );

        return csf.getFeatureVector();
    }

    @Override
    public void before() {
    }

    @Override
    public void after() {
        ClipPerFrameSpectrumFeature total = null;
        for (ClipPerFrameSpectrumFeature csf : clipToFeature.values()) {
            if (total == null) {
                total = new ClipPerFrameSpectrumFeature(csf);
            } else {
                total.addSelf(csf);
            }
        }
        
        int[] totalFeatureVector = new int[framesCount * freqsCount];

        for (int i = 0; i < total.getFramesCount(); ++i) {
            FrameSpectrum fs = total.getFrame(i);
            int[] idx = ArrayUtil.argsort(fs.data, false);
            
            System.arraycopy(idx, 0, totalFeatureVector, i * freqsCount, freqsCount);
        }

        this.featureVectorIdx = totalFeatureVector;
    }
    
    @Override
    public boolean alreadyExtracted() {
        return featureVectorIdx != null;
    }

    public void saveFreqFile(File freqFile) throws IOException {
        final FileWriter fw = new FileWriter(freqFile);
        final BufferedWriter bw = new BufferedWriter(fw);
        
        ClipPerFrameSpectrumFeature csf = (ClipPerFrameSpectrumFeature)clipToFeature.values().toArray()[0];
        double freqs[] = csf.getFreqs();
        
        for (int i = 0; i < featureVectorIdx.length; ++i) {
            bw.write(String.valueOf(featureVectorIdx[i]));
            bw.write(",");
            bw.write(String.valueOf(freqs[featureVectorIdx[i]]));
            bw.write("\n");
        }
        
        bw.flush();
    }
}
