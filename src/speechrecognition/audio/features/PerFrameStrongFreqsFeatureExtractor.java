/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package speechrecognition.audio.features;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import speechrecognition.audio.Clip;
import speechrecognition.audio.FrameSpectrum;
//import java.util.List;
import speechrecognition.util.ArrayUtil;

/**
 *
 * @author davinchi
 *
 * TODO make this generic (on Feature) class?
 */
public class PerFrameStrongFreqsFeatureExtractor 
            extends PerFrameFeatureExtractor<ClipPerFrameSpectrumFeatures> {

    private int freqsCount;
    private int[] featureVectorIdx = null;
    
    public PerFrameStrongFreqsFeatureExtractor(int[] freqs, int framesCount) {
        super(framesCount);
        
        this.freqsCount = freqs.length;
        this.featureVectorIdx = freqs;
    }

    public PerFrameStrongFreqsFeatureExtractor(int freqsCount, int framesCount) {
        super(framesCount);

        this.freqsCount = freqsCount;
    }

    @Override
    public void featureFromClip(Clip clip) {
        ClipPerFrameSpectrumFeatures csf = new ClipPerFrameSpectrumFeatures(clip, framesCount);
        clipToFeature.put(clip, csf);
    }
    
    @Override
    public double[] extractFeatureVector(Clip clip) {
        ClipPerFrameSpectrumFeatures csf = new ClipPerFrameSpectrumFeatures(
                clipToFeature.get(clip),
                this.featureVectorIdx
        );

        return csf.getFeatureVector();
    }

    

    @Override
    public void analyzeFeatureVector() {
        ClipPerFrameSpectrumFeatures total = null;
        for (ClipPerFrameSpectrumFeatures csf : clipToFeature.values()) {
            if (total == null) {
                total = new ClipPerFrameSpectrumFeatures(csf);
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
    public boolean canSerialize() {
        return this.featureVectorIdx != null;
    }
    
    @Override
    public void serializeParameters(OutputStream os) throws IOException {
        final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

        ClipSpectrumFeature csf = (ClipSpectrumFeature) clipToFeature.values().toArray()[0];

        double freqs[] = csf.getFreqs();
        
        bw.write(String.valueOf(freqsCount));
        bw.write("\n");

        for (int i = 0; i < featureVectorIdx.length; ++i) {
            bw.write(String.valueOf(featureVectorIdx[i]));
            bw.write(",");
            bw.write(String.valueOf(freqs[featureVectorIdx[i]]));
            bw.write("\n");
        }

        bw.flush();
    }

    //@Override
    public static FeaturesExtractor fromStream(final BufferedReader br) throws IOException {
        
        List<Integer> idxList = new ArrayList<Integer>();
   
        int framesCount = Integer.parseInt(br.readLine());
        
        try {
            String str = br.readLine();
            String[] parts = str.split(",");
            idxList.add(Integer.parseInt(parts[0]));
        } catch (EOFException eof) {
        }

        int idx[] = new int[idxList.size()];
        for(int i = 0; i < idx.length; ++i) {
            idx[i] = idxList.get(i);
        }
        
        return new PerFrameStrongFreqsFeatureExtractor(idx, framesCount);
    }
}
