/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package speechrecognition;

import java.io.File;
import java.io.IOException;
import java.io.FilenameFilter;
import java.io.*;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import javax.sound.sampled.UnsupportedAudioFileException;

import speechrecognition.spectro.Clip;
import speechrecognition.spectro.ClipSpectrum;
import speechrecognition.util.ArrayUtil;

/**
 *
 * @author davinchi
 */
public class SoundBase {

    private final File rootDir;
    private ClipSpectrum sumSpectrum;
    private final Map<String, Map<Integer, List<ClipSpectrum>>> clips;
    private double[] freqs;
    
    private List<ClipSpectrum> loadNumberClips(File numberDir) {
        List<ClipSpectrum> result = new ArrayList<ClipSpectrum>();
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".wav");
            }
        };

        for (File wav : numberDir.listFiles(filter)) {
            try {
                Clip clip = Clip.newInstance(wav);
                System.out.println("Read file " + wav);
                ClipSpectrum cs = new ClipSpectrum(clip);
                
                if (freqs == null) {
                    freqs = cs.getFreqs();
                }
                
                result.add(cs);
            } catch (UnsupportedAudioFileException e) {
                System.out.println(e);
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        return result;
    }

    private Map<Integer, List<ClipSpectrum>> parseDictor(File dictorDir) {
        HashMap<Integer, List<ClipSpectrum>> result = new HashMap<Integer, List<ClipSpectrum>>();

        for (File numberDir : dictorDir.listFiles()) {
            if (!numberDir.isDirectory()) {
                continue;
            }

            List<ClipSpectrum> numberClips = loadNumberClips(numberDir);
            Integer number = Integer.parseInt(numberDir.getName());

            result.put(number, numberClips);
        }

        return result;
    }

    public SoundBase(String dirname) {
        rootDir = new File(dirname);
        clips = new HashMap<String, Map<Integer, List<ClipSpectrum>>>();
        //sumSpectrum = null;

        // TODO probably make lazy initialization
        this.initDatabase();
    }

    private void initDatabase() {
        for (File dictorDir : rootDir.listFiles()) {

            if (!dictorDir.isDirectory()) {
                continue;
            }

            Map<Integer, List<ClipSpectrum>> dictor = parseDictor(dictorDir);
            String dictorName = dictorDir.getName();

            clips.put(dictorName, dictor);
            //System.out.println(f.getAbsolutePath());
            //System.out.println(f.getName());
        }
    }

    public final Map<String, Map<Integer, List<ClipSpectrum>>> getClips() {
        return clips;
    }

    // TODO rewrite these as classes
    public int[] getStrongestFreqsIdx() {

        for (Map<Integer, List<ClipSpectrum>> map : clips.values()) {
            for (List<ClipSpectrum> list : map.values()) {
                for (ClipSpectrum cs : list) {
                    if (sumSpectrum == null) {
                        sumSpectrum = new ClipSpectrum(cs);
                    } else {
                        sumSpectrum.addSelf(cs);
                    }
                }
            }
        }

        double[] data = sumSpectrum.getData();

        int[] idx = ArrayUtil.argsort(data, false);

        return idx;
    }

    public double[] getStrongestFreqs(int amount) {
        int[] idx = getStrongestFreqsIdx();

//        double[] freqs = freqs;

        double[] result_freqs = new double[amount];
        
        for (int i = 0; i < amount; ++i) {
            result_freqs[i] = freqs[idx[i]];
        }

        return result_freqs;
    }
    
    public int[] getStrongestFreqsSecondIdx(int amount) {
        double[] result = new double[Clip.DEFAULT_FRAME_SIZE]; // TODO fix this
        Arrays.fill(result, 0);
        
        for (Map<Integer, List<ClipSpectrum>> map : clips.values()) {
            for (List<ClipSpectrum> list : map.values()) {
                for (ClipSpectrum cs : list) {
                    double data[] = cs.getData();
                    int idx[] = ArrayUtil.argsort(data, false);
                    
                    for(int i = 0; i < amount; ++i) {
                        result[ idx[i] ] += 1;
                    }
                }
            }
        }
        
        int idx[] = ArrayUtil.argsort(result, false);
        return Arrays.copyOfRange(idx, 0, amount);
    }

    public double[] getStrongestFreqsSecond(int amount) {
        int[] idx = getStrongestFreqsSecondIdx(amount);

 //       double[] freqs = sumSpectrum.getFreqs();

        double[] result_freqs = new double[amount];
        
        for (int i = 0; i < amount; ++i) {
            result_freqs[i] = freqs[idx[i]];
        }

        return result_freqs;
    }


    public void writeNeurophCSV(String filename, int amount) {
        writeNeurophCSV(new File(filename), amount);
    }

    private void writeNeurophCSV(File out, int amount) {
        int idx[] = getStrongestFreqsSecondIdx(amount);
        
        final int DICTORS_COUNT = clips.size();
        final int NUMBERS_COUNT = 10;
        final int TOTAL_OUTPUT = DICTORS_COUNT * NUMBERS_COUNT;
        
        try {
            out.createNewFile();
            FileWriter fw = new FileWriter(out.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            int dictorN = 0;
            
            for (Map.Entry<String, Map<Integer, List<ClipSpectrum>>> dictorEntry: clips.entrySet()) {
                //String dictorName = dictorEntry.getKey();
                for (Map.Entry<Integer, List<ClipSpectrum>> numberEntry: dictorEntry.getValue().entrySet()) {
                    Integer number = numberEntry.getKey();
                    for (ClipSpectrum cs : numberEntry.getValue()) {
                        final double[] data = cs.getData();
                        
                        
                        for(int i = 0; i < amount; ++i) {
                            bw.write( String.valueOf( data[ idx[i] ] ) );
                            bw.write(",");
                        }
                        for(int i = 0; i < TOTAL_OUTPUT; ++i) {
                            final int d = i / NUMBERS_COUNT;
                            final int n = i % NUMBERS_COUNT;
                            bw.write( (d == dictorN && n == number) ? "1" : "0");
                            if (i != TOTAL_OUTPUT - 1)
                                bw.write(",");
                        }
                        /*
                        for(int i = 0; i < DICTORS_COUNT; ++i) {
                            bw.write(i == dictorN ? "1" : "0");
                            if (i != DICTORS_COUNT - 1)
                                bw.write(",");
                        }
                        bw.write(",");
                        for(int i = 0; i < NUMBERS_COUNT; ++i) {
                            bw.write(i == number ? "1" : "0");
                            if (i != NUMBERS_COUNT - 1)
                                bw.write(",");
                        }*/
                        bw.write("\n");
                    }
                }
                dictorN++;
            }
            bw.flush();

            //         pw.println(Arrays.toString(data));
//         pw.println(Arrays.toString(freqs));
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
