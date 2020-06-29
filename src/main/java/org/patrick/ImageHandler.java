package org.patrick;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public final class ImageHandler {
    
    public static BufferedImage loadImageAsBi(String path) throws IOException {
        BufferedImage img = ImageIO.read(new File(path));
        return img;        
    }
    
    public static InputStream loadImageAsIs(String path) {
        InputStream is = null;
        return is;        
    }
    
    public static void saveImage(BufferedImage bi, String path) throws IOException {
        File file = new File(path);
        ImageIO.write(bi, "png", file);
    }
    
    private ImageHandler() {        
    }

}
