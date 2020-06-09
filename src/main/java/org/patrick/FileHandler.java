package org.patrick;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public final class FileHandler {
    
    public static ArrayList<String> readFile(String path) throws IOException {
        String line;
        FileReader reader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(reader);
        ArrayList<String> content = new ArrayList<String>();
        
        while ((line = bufferedReader.readLine()) != null) {
            content.add(line);
        }
        
        reader.close();
        
        return content;
    }
    
    public static void writeFile(String path, ArrayList<String> content) throws IOException {
        FileWriter fw = new FileWriter(path);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw);
        
        for (String line : content) {
            out.println(line);
        }
        
        out.close();
    }
    
    private FileHandler() {
    }

}
