package org.patrick;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author Patrick
 * 
 * Generates a random frequency
 *
 */
public final class Frequency {
    
    private static String cfgPath = "frequency.cfg";
    private static String blacklistPath = "blacklist.cfg";
    
    /**
     * @return the cfgPath
     */
    public static String getCfgPath() {
        return cfgPath;
    }

    /**
     * @return the blacklistPath
     */
    public static String getBlacklistPath() {
        return blacklistPath;
    }

    public static double getFrequency() throws IOException {
        
        double frequency;
        do {
            frequency = randomFrequency(getMinFF(), getMaxFF());
        } while (isBlacklisted(frequency));   
        return frequency;
    }
    
    public static double getMinFF() throws IOException {
        ArrayList<String> content = FileHandler.readFile(getCfgPath());
        double d0 = Double.parseDouble(content.get(0));
        double d1 = Double.parseDouble(content.get(1));
        
        int compareVal = Double.compare(d0, d1);
        double min;
        
        if (compareVal > 0) {
            min = d1;
        } else if (compareVal < 0) {
            min = d0;
        } else {
            min = d0;
        }
        
        return min;
    }
    
    public static double getMaxFF() throws IOException {
        ArrayList<String> content = FileHandler.readFile(getCfgPath());
        double d0 = Double.parseDouble(content.get(0));
        double d1 = Double.parseDouble(content.get(1));
        
        int compareVal = Double.compare(d0, d1);
        double max;
        
        if (compareVal > 0) {
            max = d0;
        } else if (compareVal < 0) {
            max = d1;
        } else {
            max = d0;
        }
        
        return max;
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
    
    private static boolean isBlacklisted(double frequenz) throws IOException {
        ArrayList<String> content = FileHandler.readFile(getBlacklistPath());
        for (String ffString : content) {
            double ff = Double.parseDouble(ffString);
            if (ff == frequenz) {
                return true;
            }
        }
        
        return false;
    }
    
    private Frequency() {        
    }

}