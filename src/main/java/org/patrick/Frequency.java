package org.patrick;

import java.util.Random;

/**
 * @author Patrick
 * 
 * Generates a random frequency
 *
 */
public final class Frequency {
    
    private static double[] blacklist = {40.0, 45.0, 70.6, 77.7, 87.0};
    
    public static double getFrequency() {
        double frequency;
        do {
            frequency = randomFrequency(30.0, 87.0);
        } while (isBlacklisted(frequency));   
        return frequency;
    }
    
    private static double round(double value) {
        double tmpD = value * 10;
        long tmpL = Math.round(tmpD); 
        return (double) tmpL / 10;
    }
    
    private static double randomFrequency(double min, double max) {
        Random r = new Random();
        double frequenz = min + r.nextFloat() * (max - min);
        frequenz = round(frequenz);
        return frequenz;
    }
    
    private static boolean isBlacklisted(double frequenz) {
        for (double d: blacklist) {
            if (d == frequenz) {
                return true;
            }                   
        }
        return false;
    }
    
    private Frequency() {        
    }

}