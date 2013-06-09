/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package speechrecognition.audio.features;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import java.io.File;

import java.io.OutputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.io.IOException;
import java.io.EOFException;
import speechrecognition.audio.Clip;
//import java.util.List;
import speechrecognition.util.ArrayUtil;

/**
 *
 * @author davinchi
 *
 * TODO make this generic (on Feature) class?
 */
public class SpectrumFeatureExtractor extends FeaturesExtractor {

    private final int freqsCount;
    private int[] featureVectorIdx = null;
    private Map<Clip, ClipSpectrumFeature> clipToFeature =
            new HashMap<Clip, ClipSpectrumFeature>();
    
    public SpectrumFeatureExtractor(int[] featureFreqs) {
        this.freqsCount = featureFreqs.length;
        this.featureVectorIdx = featureFreqs;
    }

    public SpectrumFeatureExtractor(int freqsCount) {
        this.freqsCount = freqsCount;
    }

    @Override
    public void featureFromClip(Clip clip) {
        clipToFeature.put(clip, new ClipSpectrumFeature(clip));
    }

    @Override
    public double[] extractFeatureVector(Clip clip) {
        ClipSpectrumFeature csf = new ClipSpectrumFeature(
                clipToFeature.get(clip),
                this.featureVectorIdx);

        return csf.getFeatureVector();
    }

    @Override
    public void analyzeFeatureVector() {
        ClipSpectrumFeature total = null;
        for (ClipSpectrumFeature csf : clipToFeature.values()) {
            if (total == null) {
                total = new ClipSpectrumFeature(csf);
            } else {
                total.addSelf(csf);
            }
        }

        double[] data = total.getData();

        int[] idx = ArrayUtil.argsort(data, false);

        this.featureVectorIdx = Arrays.copyOfRange(idx, 0, freqsCount);
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
        
        return new SpectrumFeatureExtractor(idx);
    }
}
