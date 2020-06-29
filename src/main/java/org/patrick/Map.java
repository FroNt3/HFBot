package org.patrick;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Map {
    
    private BufferedImage map;
    private int size;
    private String exportPath;
    
    
    public Map(String path, int size) throws IOException {
        this.map = ImageHandler.loadImageAsBi(path);
        this.size = size;
        String tmpPath = path;
        tmpPath = tmpPath.replaceFirst("\\..*", "");
        tmpPath = tmpPath + "_hfb.png";
        this.exportPath = tmpPath;
    }
    
    public void drawDot(int x, int y, int size) {
        Graphics2D g2d = map.createGraphics();
        int invY = this.size - y;
        
        g2d.setPaint(Color.red);
        g2d.fillOval(x - 8, invY - 8, size, size);        
        g2d.dispose();   
    }
    
    public void drawName(int x, int y, String name) {
        Graphics2D g2d = map.createGraphics();
        int invY = this.size - y;
        
        g2d.setPaint(Color.red);     
        g2d.setFont(new Font("Serif", Font.BOLD, 20));
        g2d.drawString(name, x + 10, invY - 10);        
        g2d.dispose();       
    }
    
    public void drawPos(int x, int y, int size, String name) {
        this.drawDot(x, y, size);
        this.drawName(x, y, name);
    }
    
    public int relativizePos(double pos, double max) {
        double rel = pos / max;
        long tmpL = Math.round(rel * 100000); 
        rel = (double) tmpL / 100000;      
        
        int x = (int) Math.round((this.size * rel));
        
        return x;
    }
    
    public void export() throws IOException {
        ImageHandler.saveImage(this.map, this.exportPath);
    }
    
}
