/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package speechrecognition.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 *
 * @author davinchi
 */
public class ClipPlayback implements Runnable {

    SourceDataLine line;
    Thread thread;
    AudioInputStream audioInputStream;
    
    final int bufSize = 16384;
    
    public ClipPlayback(AudioInputStream audioInputStream) {
        this.audioInputStream = audioInputStream;
    }

    public void start() {
        thread = new Thread(this);
        thread.setName("Playback");
        thread.start();
    }
    
    public void stop() {
        thread = null;
    }

    public void run() {
        try {
            audioInputStream.reset();
        } catch (Exception e) {
            return;
        }

        AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
        float rate = 44100.0f;
        int channels = 2;
        int frameSize = 4;
        int sampleSize = 16;
        boolean bigEndian = true;

        AudioFormat format = new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8)
                * channels, rate, bigEndian);

        AudioInputStream playbackInputStream = AudioSystem.getAudioInputStream(format,
                audioInputStream);

        if (playbackInputStream == null) {
            //shutDown("Unable to convert stream of format " + audioInputStream + " to format " + format);
            return;
        }

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            //shutDown("Line matching " + info + " not supported.");
            return;
        }

        // get and open the source data line for playback.

        try {
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format, bufSize);
        } catch (LineUnavailableException ex) {
            //shutDown("Unable to open the line: " + ex);
            return;
        }

        // play back the captured audio data

        int frameSizeInBytes = format.getFrameSize();
        int bufferLengthInFrames = line.getBufferSize() / 8;
        int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
        byte[] data = new byte[bufferLengthInBytes];
        int numBytesRead = 0;

        line.start();

        while (thread != null) {
            try {
                if ((numBytesRead = playbackInputStream.read(data)) == -1) {
                    break;
                }
                int numBytesRemaining = numBytesRead;
                while (numBytesRemaining > 0) {
                    numBytesRemaining -= line.write(data, 0, numBytesRemaining);
                }
            } catch (Exception e) {
                //shutDown("Error during playback: " + e);
                break;
            }
        }
        // we reached the end of the stream.
        // let the data play out, then
        // stop and close the line.
        if (thread != null) {
            line.drain();
        }
        line.stop();
        line.close();
        line = null;
    }
}
