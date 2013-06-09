/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package speechrecognition.audio.features;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
//import java.util.Arrays;
//import java.util.Map;
//import java.util.HashMap;
import speechrecognition.audio.Clip;
//import java.util.List;
//import speechrecognition.util.ArrayUtil;

/**
 *
 * @author davinchi
 *
 * TODO make this generic (on Feature) class?
 */
public class PerFrameBandsFeatureExtractor
        extends PerFrameFeatureExtractor<ClipPerFrameBandsFeatures> {

    private double[] featureRanges;
    private boolean extracted = false;

    public PerFrameBandsFeatureExtractor(int framesCount) {
        super(framesCount);
        this.featureRanges = null;
    }

    public PerFrameBandsFeatureExtractor(String ranges, int framesCount) {
        super(framesCount);
        
        String[] parts = ranges.split(",");
        assert(parts.length >= 2);
        
        this.featureRanges = new double[parts.length];
        for(int i = 0; i < parts.length; ++i) {
            featureRanges[i] = Double.parseDouble(parts[i]);
        }
        for(int i = 0; i < parts.length - 1; ++i) {
            assert(featureRanges[i] < featureRanges[i - 1]);
        }
    }

    @Override
    public void featureFromClip(Clip clip) {
        ClipPerFrameBandsFeatures csf = new ClipPerFrameBandsFeatures(
                clip, framesCount);
        clipToFeature.put(clip, csf);
    }

    @Override
    public double[] extractFeatureVector(Clip clip) {
        ClipPerFrameBandsFeatures csf = new ClipPerFrameBandsFeatures(
                clipToFeature.get(clip),
                this.featureRanges);

        return csf.getFeatureVector();
    }

    @Override
    public void analyzeFeatureVector() {
    }
    
    @Override
    public boolean canSerialize() {
        return true;
    }

    @Override
    public void serializeParameters(OutputStream os) throws IOException {
        //final FileWriter fw = new FileWriter(os);
        
        //final OutputStreamWriter osw = new OutputStreamWriter(os);
        final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
        
        
        bw.write(String.valueOf(this.framesCount));
        bw.write("\n");

        for(int i = 0; i < featureRanges.length; ++i) {
            bw.write(String.valueOf(featureRanges[i]));
            if (i != featureRanges.length - 1)
                bw.write(",");
        }
        bw.write("\n");
        
        bw.flush();
    }
    
    //@Override
    public static FeaturesExtractor fromStream(final BufferedReader br) throws IOException {
        int framesCount = Integer.parseInt(br.readLine());
        String features = br.readLine();
        return new PerFrameBandsFeatureExtractor(features, framesCount);
    }
}
