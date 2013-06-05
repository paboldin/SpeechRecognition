/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package speechrecognition.util;

import java.util.Arrays;
import java.util.Comparator;
//import java.util.Random;


/**
 *
 * @author davinchi, taken from array4j
 */
public final class ArrayUtil {
    public static int[] argsort(final double[] a) {
        return argsort(a, true);
    }

    public static int[] argsort(final double[] a, final boolean ascending) {
        Integer[] indexes = new Integer[a.length];
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = i;
        }
        Arrays.sort(indexes, new Comparator<Integer>() {
            @Override
            public int compare(final Integer i1, final Integer i2) {
                return (ascending ? 1 : -1) * Double.compare(a[i1], a[i2]);
            }
        });
        return asArray(indexes);
    }

    public static <T extends Number> int[] asArray(final T... a) {
        int[] b = new int[a.length];
        for (int i = 0; i < b.length; i++) {
            b[i] = a[i].intValue();
        }
        return b;
    }

}
