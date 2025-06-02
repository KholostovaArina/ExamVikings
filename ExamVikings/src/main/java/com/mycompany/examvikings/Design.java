package com.mycompany.examvikings;

import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Design {
    private static Image rightImage;
    private static Image leftImage;
    
    static {
        try {
            rightImage = ImageIO.read(Design.class.getResourceAsStream("/право.png"));
            leftImage = ImageIO.read(Design.class.getResourceAsStream("/лево.png"));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static Image getLeftImage() {
        return leftImage;
    }
    
    public static Image getRightImage() {
        return rightImage;
    }
}
