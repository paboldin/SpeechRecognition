/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package speechrecognition.spectro;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
//import java.util.List;
import speechrecognition.util.ArrayUtil;

/**
 *
 * @author davinchi
 * 
 * TODO make this generic (on Feature) class?
 */
public class SpectrumFeatureExtractor implements FeatureExtractor {
    private final int freqsCount;
    private int[] featureVectorIdx;
    
    private Map<Clip, ClipSpectrumFeature> clipToFeature =
            new HashMap<Clip, ClipSpectrumFeature>();
    
    public SpectrumFeatureExtractor(int freqsCount) {
        this.freqsCount = freqsCount;
        this.featureVectorIdx = null;
    }
    
    @Override
    public void featureFromClip(Clip clip) {
        clipToFeature.put(clip, new ClipSpectrumFeature(clip));
    }

    
    @Override
    public double[] extractFeatureVector(Clip clip) {
        ClipSpectrumFeature csf = new ClipSpectrumFeature(
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
        ClipSpectrumFeature total = null;
        for(ClipSpectrumFeature csf: clipToFeature.values()) {
            if (total == null)
                total = new ClipSpectrumFeature(csf);
            else
                total.addSelf(csf);
        }
        
        double[] data = total.getData();

        int[] idx = ArrayUtil.argsort(data, false);

        this.featureVectorIdx = Arrays.copyOfRange(idx, 0, freqsCount);
    }
    
    @Override
    public boolean alreadyExtracted() {
        return featureVectorIdx != null;
    }
    
    public void saveFreqFile(File freqFile) throws IOException {
        final FileWriter fw = new FileWriter(freqFile);
        final BufferedWriter bw = new BufferedWriter(fw);
        
        ClipSpectrumFeature csf = (ClipSpectrumFeature)clipToFeature.values().toArray()[0];
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


//    public double[] getStrongestFreqs(int amount) {
//        int[] idx = getStrongestFreqsIdx();
//
////        double[] freqs = freqs;
//
//        double[] result_freqs = new double[amount];
//        
//        for (int i = 0; i < amount; ++i) {
//            result_freqs[i] = freqs[idx[i]];
//        }
//
//        return result_freqs;
//    }
//    
//    public int[] getStrongestFreqsSecondIdx(int amount) {
//        double[] result = new double[Clip.DEFAULT_FRAME_SIZE]; // TODO fix this
//        Arrays.fill(result, 0);
//        
//        for (Map<Integer, List<ClipFeature>> map : clips.values()) {
//            for (List<ClipFeature> list : map.values()) {
//                for (ClipFeature cs : list) {
//                    double data[] = cs.getData();
//                    int idx[] = ArrayUtil.argsort(data, false);
//                    
//                    for(int i = 0; i < amount; ++i) {
//                        result[ idx[i] ] += 1;
//                    }
//                }
//            }
//        }
//        
//        int idx[] = ArrayUtil.argsort(result, false);
//        return Arrays.copyOfRange(idx, 0, amount);
//    }
//
//    public double[] getStrongestFreqsSecond(int amount) {
//        int[] idx = getStrongestFreqsSecondIdx(amount);
//
// //       double[] freqs = sumSpectrum.getFreqs();
//
//        double[] result_freqs = new double[amount];
//        
//        for (int i = 0; i < amount; ++i) {
//            result_freqs[i] = freqs[idx[i]];
//        }
//
//        return result_freqs;
//    }
//
