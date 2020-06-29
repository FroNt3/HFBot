package org.patrick;

public final class Calc {
    
    public static String formatInt(int number) {
        String balanceUnformat = Integer.toString(number);
        String balanceReverse = "";
        String balanceFormat = "";
        int counter = 0;
        
        for (int i = (balanceUnformat.length() - 1); i >= 0; i--) {
            balanceReverse = balanceReverse + balanceUnformat.charAt(i);
            if (++counter == 3 && i != 0) {
                balanceReverse = balanceReverse + ".";
                counter = 0;
            }
        }
        
        for (int i = (balanceReverse.length() - 1); i >= 0; i--) {
            balanceFormat = balanceFormat + balanceReverse.charAt(i);
        }      
        
        return balanceFormat;
    }
    
    private Calc() {
    }

}
