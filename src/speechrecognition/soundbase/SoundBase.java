/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package speechrecognition.soundbase;

import java.io.File;
import java.io.IOException;
import java.io.FilenameFilter;
import java.io.*;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
//import java.util.Arrays;

import javax.sound.sampled.UnsupportedAudioFileException;

import speechrecognition.spectro.Clip;
//import speechrecognition.spectro.ClipFeature;
import speechrecognition.spectro.FeatureExtractor;
//import speechrecognition.util.ArrayUtil;
//import speechrecognition.soundbase.SoundBaseVisitor;

/**
 *
 * @author davinchi
 */
public class SoundBase<T extends FeatureExtractor> {

    private final T featureExtractor;
    private final File rootDir;
//    private ClipFeature sumSpectrum;
    private final Map<String, Map<Integer, List<Clip>>> clips;
//    private double[] freqs;

    public enum WriterType {

        PERDICTOR_PERNUMBER,
        PERNUMBER,
        PERDICTOR
    }

    private List<Clip> loadNumberClips(File numberDir) {
        List<Clip> result = new ArrayList<Clip>();
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".wav");
            }
        };

        for (File wav : numberDir.listFiles(filter)) {
            try {
                Clip clip = Clip.newInstance(wav);
                //System.out.println("Read file " + wav);
                //ClipFeature cs = new ClipFeature(clip);

                //               if (freqs == null) {
                //                   freqs = cs.getFreqs();
                //               }

                result.add(clip);
            } catch (UnsupportedAudioFileException e) {
                System.out.println(e);
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        return result;
    }

    private Map<Integer, List<Clip>> parseDictor(File dictorDir) {
        HashMap<Integer, List<Clip>> result = new HashMap<Integer, List<Clip>>();

        for (File numberDir : dictorDir.listFiles()) {
            if (!numberDir.isDirectory()) {
                continue;
            }

            List<Clip> numberClips = loadNumberClips(numberDir);
            Integer number = Integer.parseInt(numberDir.getName());

            result.put(number, numberClips);
        }

        return result;
    }

    private void initDatabase() {
        for (File dictorDir : rootDir.listFiles()) {

            if (!dictorDir.isDirectory()) {
                continue;
            }

            Map<Integer, List<Clip>> dictor = parseDictor(dictorDir);
            String dictorName = dictorDir.getName();

            clips.put(dictorName, dictor);
            //System.out.println(f.getAbsolutePath());
            //System.out.println(f.getName());
        }
    }

    public SoundBase(String dirname, T featureExtractor) {
        rootDir = new File(dirname);
        clips = new HashMap<String, Map<Integer, List<Clip>>>();
        this.featureExtractor = featureExtractor;
        //sumSpectrum = null;

        // TODO probably make lazy initialization
        this.initDatabase();
    }

    public final Map<String, Map<Integer, List<Clip>>> getClips() {
        return clips;
    }

    private <T extends SoundBaseVisitor> void visitClips(T visitor) {
        for (Map.Entry<String, Map<Integer, List<Clip>>> dictorEntry : clips.entrySet()) {
            String dictorName = dictorEntry.getKey();
            visitor.visitDictor(dictorName);
            for (Map.Entry<Integer, List<Clip>> numberEntry : dictorEntry.getValue().entrySet()) {
                Integer number = numberEntry.getKey();
                visitor.visitNumber(number);
                for (Clip clip : numberEntry.getValue()) {
                    visitor.visitClip(clip);
                }
            }
        }
    }

    public void extractFeatures() {

        SoundBaseVisitor visitor = new SoundBaseVisitor() {
            @Override
            public void visitDictor(String name) {
            }

            @Override
            public void visitNumber(Integer number) {
            }

            @Override
            public void visitClip(Clip clip) {
                featureExtractor.featureFromClip(clip);
            }
        };

        featureExtractor.before();
        visitClips(visitor);
        featureExtractor.after();
    }

    public void writeNeurophCSV(String filename) {
        writeNeurophCSV(new File(filename), WriterType.PERDICTOR_PERNUMBER);
    }

    private void writeNeurophCSV(File out, WriterType v) {
        extractFeatures();

        try {
            out.createNewFile();
            final FileWriter fw = new FileWriter(out.getAbsoluteFile());
            final BufferedWriter bw = new BufferedWriter(fw);
            NeurophCSVWriter writer = null;

            switch (v) {
                case PERDICTOR_PERNUMBER:
                    writer = new NeurophCSVWriterPerDictorPerNumber(featureExtractor, bw,
                            clips.size(), 10);
                    break;

                case PERDICTOR:
                    writer = new NeurophCSVWriterPerDictor(featureExtractor, bw,
                            clips.size(), 10);
                    break;

                case PERNUMBER:
                    writer = new NeurophCSVWriterPerNumber(featureExtractor, bw,
                            clips.size(), 10);
                    break;
            }

            if (writer != null) {
                visitClips(writer);
            }
            
            bw.flush();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private abstract static class NeurophCSVWriter implements SoundBaseVisitor {

        final int DICTORS_COUNT;
        final int NUMBERS_COUNT;
        final int TOTAL_OUTPUT;
        protected int dictorNumber = -1; // to unconditionally shift them in next*
        protected int numberN = -1;
        FeatureExtractor featureExtractor;
        BufferedWriter bw;

        public NeurophCSVWriter(FeatureExtractor featureExtractor, BufferedWriter bw, int dictors_count, int numbers_count) {
            this.featureExtractor = featureExtractor;
            this.bw = bw;

            DICTORS_COUNT = dictors_count;
            NUMBERS_COUNT = numbers_count;
            TOTAL_OUTPUT = DICTORS_COUNT * NUMBERS_COUNT;
        }

        @Override
        public void visitDictor(String name) {
            dictorNumber++;
            numberN = 0;
        }

        @Override
        public void visitNumber(Integer number) {
            numberN = number;
        }

        public abstract void writeOutputVector() throws IOException;

        @Override
        public void visitClip(Clip clip) {
            double[] features = featureExtractor.extractFeatureVector(clip);

            try {
                for (int i = 0; i < features.length; ++i) {
                    bw.write(String.valueOf(features[i]));
                    if (i != features.length - 1) {
                        bw.write(",");
                    }
                }

                writeOutputVector();

                bw.write("\n");
            } catch (IOException e) {
            }
        }
    };

    public static class NeurophCSVWriterPerDictorPerNumber extends NeurophCSVWriter {

        public NeurophCSVWriterPerDictorPerNumber(FeatureExtractor featureExtractor, BufferedWriter bw, int dictors_count, int numbers_count) {
            super(featureExtractor, bw, dictors_count, numbers_count);
        }

        @Override
        public void writeOutputVector() throws IOException {
            bw.write(",");

            for (int i = 0; i < TOTAL_OUTPUT; ++i) {
                final int d = i / NUMBERS_COUNT;
                final int n = i % NUMBERS_COUNT;
                bw.write((d == dictorNumber && n == numberN) ? "1" : "0");
                if (i != TOTAL_OUTPUT - 1) {
                    bw.write(",");
                }
            }
        }
    };

    public static class NeurophCSVWriterPerDictor extends NeurophCSVWriter {

        public NeurophCSVWriterPerDictor(FeatureExtractor featureExtractor, BufferedWriter bw, int dictors_count, int numbers_count) {
            super(featureExtractor, bw, dictors_count, numbers_count);
        }

        @Override
        public void writeOutputVector() throws IOException {
            bw.write(",");

            for (int d = 0; d < DICTORS_COUNT; ++d) {
                bw.write(d == dictorNumber ? "1" : "0");
                if (d != DICTORS_COUNT - 1) {
                    bw.write(",");
                }
            }
        }
    };

    public static class NeurophCSVWriterPerNumber extends NeurophCSVWriter {

        public NeurophCSVWriterPerNumber(FeatureExtractor featureExtractor, BufferedWriter bw, int dictors_count, int numbers_count) {
            super(featureExtractor, bw, dictors_count, numbers_count);
        }

        @Override
        public void writeOutputVector() throws IOException {
            bw.write(",");

            for (int n = 0; n < NUMBERS_COUNT; ++n) {
                bw.write(n == numberN ? "1" : "0");
                if (n != NUMBERS_COUNT - 1) {
                    bw.write(",");
                }
            }
        }
    };
}
