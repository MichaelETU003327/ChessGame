package tools;

import java.io.File;
import java.io.IOException;
import java.util.Vector;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import pieces.Piece;

public class Function {

    //Image des pieces 
    public static BufferedImage getImage(String imagePath) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    //Test si un Object est null;
    public static boolean isset(Object object){
        return (object!=null);
    }

    

    
}
