/*
 * Created on Jul 9, 2008
 *
 * Spectro-Edit is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spectro-Edit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>. 
 */
package speechrecognition.spectro;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.emory.mathcs.jtransforms.dct.DoubleDCT_1D;

/**
 * A frame of audio data, represented in the frequency domain. The specific
 * frequency components of this frame are modifiable.
 */
public class FrameSpectrum {

    private static final Logger logger = Logger.getLogger(FrameSpectrum.class.getName());
    /**
     * Array of spectral data.
     */
    protected double[] data;
    protected double power;
    /**
     * Maps frame size to the DCT instance that handles that size.
     */
    private static Map<Integer, DoubleDCT_1D> dctInstances = new HashMap<Integer, DoubleDCT_1D>();
    private final WindowFunction windowFunc;

    public FrameSpectrum(FrameSpectrum other) {
        this.data = new double[other.data.length];
        this.power = other.power;
        this.windowFunc = other.windowFunc;

        System.arraycopy(other.data, 0, this.data, 0, this.data.length);
    }
    
    public void addSelf(FrameSpectrum other) {
        assert(this.data.length == other.data.length);
        for(int i = 0; i < this.data.length; ++i) {
            this.data[i] = Math.sqrt(Math.pow(this.data[i], 2) + Math.pow(other.data[i], 2));
        }
        this.power = Math.sqrt(Math.pow(this.power, 2) + Math.pow(other.power, 2));
    }

    public FrameSpectrum(double[] timeData, WindowFunction windowFunc, int n) {
        this.windowFunc = windowFunc;
        int frameSize = timeData.length;
        DoubleDCT_1D dct = getDctInstance(frameSize);

        // in place window
        windowFunc.applyWindow(timeData);

        // in place transform: timeData becomes frequency data
        dct.forward(timeData, true);

        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;

        power = 0.0;
        data = new double[frameSize];
        for (int i = 0; i < data.length; i++) {
            data[i] = timeData[i];
            power += data[i] * data[i];
            min = Math.min(data[i], min);
            max = Math.max(data[i], max);
        }

        power = Math.sqrt(power) / n;

        if (logger.isLoggable(Level.FINER)) {
            logger.finer(String.format("Computed frame. min=%4.6f max=%4.6f", min, max));
        }
    }

    public FrameSpectrum(double[] timeData, WindowFunction windowFunc) {
        this(timeData, windowFunc, timeData.length);
    }

    private static DoubleDCT_1D getDctInstance(int frameSize) {
        DoubleDCT_1D dct = dctInstances.get(frameSize);
        if (dct == null) {
            dct = new DoubleDCT_1D(frameSize);
            dctInstances.put(frameSize, dct);
        }
        return dct;
    }

    /**
     * Returns the length of this frame, in samples.
     *
     * @return
     */
    public int getLength() {
        return data.length;
    }

    /**
     * Returns the idx'th real component of this frame's spectrum.
     */
    public double getReal(int idx) {
        return data[idx];
    }

    /**
     * Returns the idx'th imaginary component of this frame's spectrum.
     */
    public double getImag(int idx) {
        return 0.0;
    }

    /**
     * Sets the real component at idx. This method sets the new actual value,
     * although it may make sense to provide another method that scales the
     * existing value.
     *
     * @param idx The index to modify
     * @param d The new value
     */
    public void setReal(int idx, double d) {
        data[idx] = d;
    }

    /**
     * Returns the time-domain representation of this frame. Unless the spectral
     * data of this frame has been modified, the returned array will be very
     * similar to the array given in the constructor. Even if the spectral data
     * has been modified, the length of the returned array will have the same
     * length as the original array given in the constructor.
     */
    public double[] asTimeData() {
        double[] timeData = new double[data.length];
        System.arraycopy(data, 0, timeData, 0, data.length);
        DoubleDCT_1D dct = getDctInstance(data.length);
        dct.inverse(timeData, true);
        windowFunc.applyWindow(timeData);
        return timeData;
    }

    /**
     * Quick demo to show original, transformed, and inverse transformed data.
     */
    public static void main(String[] args) {
        double[] orig = new double[]{1, 2, 3, 4, 5, 0, 9, 8, 7, 6, 5, 4, 3, 2, 1, 7};
        System.out.println(Arrays.toString(orig));
        FrameSpectrum f = new FrameSpectrum(orig, new NullWindowFunction());
        System.out.println(Arrays.toString(f.data));
        System.out.println(Arrays.toString(f.asTimeData()));
    }
}
