package org.patrick;

import java.util.Date;

public final class TimeHandler {
    
    public static boolean isRecent(int unixtime, long differenceCfg) {
        Date old = new Date((long) unixtime * 1000);
        Date current = new Date();          
        long difference = current.getTime() - old.getTime();
        
        return difference <= differenceCfg;
    }
    
    
    private TimeHandler() {        
    }

}
