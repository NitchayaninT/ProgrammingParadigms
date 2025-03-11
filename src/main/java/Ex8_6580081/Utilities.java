package Ex8_6580081;

import java.awt.Image;
import javax.swing.ImageIcon;


// Interface for keeping constant values
interface MyConstants
{
    //----- Resource files
    static final String PATH           = "src/main/java/Ex8_6580081/resources/";
    static final String FILE_BG        = PATH + "background.jpg";
    static final String FILE_CH_1_MAIN = PATH + "marmite.png";
    static final String FILE_CH_1_ALT  = PATH + "crow.png";    
    static final String FILE_CH_2_MAIN = PATH + "butter.png";
    static final String FILE_CH_2_ALT  = PATH + "butterfly.png";
    static final String FILE_ITEM      = PATH + "wing.png";    
    
    //----- Sizes and locations
    static final int FRAME_WIDTH   = 800;
    static final int FRAME_HEIGHT  = 400;
    static final int GROUND_Y      = 250;
    static final int SKY_Y         = 50;
    static final int CH_WIDTH      = 60;
    static final int CH_HEIGHT     = 50;
    static final int IT_WIDTH      = 60;
    static final int IT_HEIGHT     = 50;
}


// Auxiliary class to resize image
class MyImageIcon extends ImageIcon
{
    public MyImageIcon(String fname)  { super(fname); }
    public MyImageIcon(Image image)   { super(image); }

    public MyImageIcon resize(int width, int height)
    {
        Image oldimg = this.getImage();
        Image newimg = oldimg.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        return new MyImageIcon(newimg);
    }
}