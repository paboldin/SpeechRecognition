/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package speechrecognition.audio.features;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

import java.io.BufferedReader;
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
public class PerFrameMFCCFeatureExtractor
        extends PerFrameFeatureExtractor<ClipPerFrameMFCCFeatures> {

    private int mfccCount = 12, mfccMelCount = 20;

    public PerFrameMFCCFeatureExtractor(int framesCount) {
        super(framesCount);
    }

    public PerFrameMFCCFeatureExtractor(int framesCount, int mfccCount, int mfccMelCount) {
        super(framesCount);

        this.mfccCount = mfccCount;
        this.mfccMelCount = mfccMelCount;
    }

    @Override
    public void featureFromClip(Clip clip) {
        ClipPerFrameMFCCFeatures csf = new ClipPerFrameMFCCFeatures(
                clip, framesCount);
        clipToFeature.put(clip, csf);
    }

    @Override
    public double[] extractFeatureVector(Clip clip) {
        ClipPerFrameMFCCFeatures csf = new ClipPerFrameMFCCFeatures(
                clipToFeature.get(clip),
                this.mfccCount, this.mfccMelCount);

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

        bw.write(String.valueOf(this.mfccCount));
        bw.write("\n");
        bw.write(String.valueOf(this.mfccMelCount));
        bw.write("\n");
        
        bw.flush();
    }
    
    //@Override
    public static FeaturesExtractor fromStream(final BufferedReader br) throws IOException {
        int framesCount = Integer.parseInt(br.readLine());
        int mfccCount = Integer.parseInt(br.readLine());
        int mfccMelCount = Integer.parseInt(br.readLine());
        return new PerFrameMFCCFeatureExtractor(framesCount, mfccCount, mfccMelCount);
    }
}
