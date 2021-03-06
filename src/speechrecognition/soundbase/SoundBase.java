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
import java.util.TreeMap;
//import java.util.Arrays;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.SwingWorker;

import speechrecognition.audio.Clip;
//import speechrecognition.spectro.ClipFeature;
import speechrecognition.audio.features.FeaturesExtractor;
//import speechrecognition.util.ArrayUtil;
//import speechrecognition.soundbase.SoundBaseVisitor;

/**
 *
 * @author davinchi
 */
public class SoundBase {

    private final File rootDir;
//    private ClipFeature sumSpectrum;
    private final Map<String, Map<Integer, List<Clip>>> clips;
    int totalClipsNumber = 0;
    //    private double[] freqs;

    public enum WriterType {
        PERDICTOR_PERNUMBER,
        PERNUMBER,
        PERDICTOR,
        ENCOG
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
                totalClipsNumber += 1;
            } catch (UnsupportedAudioFileException e) {
                System.out.println(e);
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        return result;
    }

    private Map<Integer, List<Clip>> parseDictor(File dictorDir) {
        Map<Integer, List<Clip>> result = new HashMap<Integer, List<Clip>>();

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

    public SoundBase(String dirname) {
        rootDir = new File(dirname);
        clips = new TreeMap<String, Map<Integer, List<Clip>>>();
        //sumSpectrum = null;

        // TODO probably make lazy initialization
        this.initDatabase();
    }

    public final Map<String, Map<Integer, List<Clip>>> getClips() {
        return clips;
    }
    
    public int getClipsCount() {
        return totalClipsNumber;
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
    
    public void extractFeatures(final FeaturesExtractor fe) {
        extractFeatures(fe, null);
    }
    
    public void extractFeatures(final FeaturesExtractor fe, final SwingWorker sw) {        
        SoundBaseVisitor visitor = new SoundBaseVisitor() {
            int clipsVisited = 0;
            
            @Override
            public void visitDictor(String name) {
            }

            @Override
            public void visitNumber(Integer number) {
            }

            @Override
            public void visitClip(Clip clip) {
                clipsVisited++;
                fe.featureFromClip(clip);
                if (sw != null)
                    sw.firePropertyChange("progress", 0, 100 * clipsVisited / totalClipsNumber);
            }
        };

        visitClips(visitor);
        fe.analyzeFeatureVector();
    }

    public void writeNeuralNetworkCSV(String filename, FeaturesExtractor fe) {
        writeNeuralNetworkCSV(new File(filename), fe, WriterType.PERDICTOR_PERNUMBER);
    }

    public void writeNeuralNetworkCSV(File out, FeaturesExtractor fe, WriterType v) {
        try {
            out.createNewFile();
            final FileWriter fw = new FileWriter(out.getAbsoluteFile());
            final BufferedWriter bw = new BufferedWriter(fw);
            NeuralCSVWriter writer = null;

            switch (v) {
                case PERDICTOR_PERNUMBER:
                    writer = new NeuralCSVWriterPerDictorPerNumber(fe, bw,
                            clips.size(), 10);
                    break;

                case PERDICTOR:
                    writer = new NeuralCSVWriterPerDictor(fe, bw,
                            clips.size(), 10);
                    break;

                case PERNUMBER:
                    writer = new NeuralCSVWriterPerNumber(fe, bw,
                            clips.size(), 10);
                    break;
                case ENCOG:
                    writer = new NeuralCSVWriterEncog(fe, bw, clips.size(), 10);
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

    private abstract static class NeuralCSVWriter implements SoundBaseVisitor {

        final int DICTORS_COUNT;
        final int NUMBERS_COUNT;
        final int TOTAL_OUTPUT;
        protected int dictorNumber = -1; // to unconditionally shift them in next*
        protected int numberN = -1;
        FeaturesExtractor featureExtractor;
        BufferedWriter bw;

        public NeuralCSVWriter(FeaturesExtractor featureExtractor, BufferedWriter bw, int dictors_count, int numbers_count) {
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
                bw.write("#" + clip.getName() + "\n");
                
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

    private static class NeuralCSVWriterPerDictorPerNumber extends NeuralCSVWriter {

        public NeuralCSVWriterPerDictorPerNumber(FeaturesExtractor featureExtractor, BufferedWriter bw, int dictors_count, int numbers_count) {
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
    
    private static class NeuralCSVWriterEncog extends NeuralCSVWriter {

        public NeuralCSVWriterEncog(FeaturesExtractor featureExtractor, BufferedWriter bw, int dictors_count, int numbers_count) {
            super(featureExtractor, bw, dictors_count, numbers_count);
        }

        @Override
        public void writeOutputVector() throws IOException {
            //bw.write(",");
            //bw.write(String.valueOf(dictorNumber));
            bw.write(",");
            bw.write(String.valueOf(numberN));
        }
    };    

    private static class NeuralCSVWriterPerDictor extends NeuralCSVWriter {

        public NeuralCSVWriterPerDictor(FeaturesExtractor featureExtractor, BufferedWriter bw, int dictors_count, int numbers_count) {
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

    private static class NeuralCSVWriterPerNumber extends NeuralCSVWriter {

        public NeuralCSVWriterPerNumber(FeaturesExtractor featureExtractor, BufferedWriter bw, int dictors_count, int numbers_count) {
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
