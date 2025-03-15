package Ex9_6580081;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.sound.sampled.*;     // for sounds


// Interface for keeping constant values
interface MyConstants
{
    //----- Resource files
    static final String PATH               = "src/main/java/Ex9/resources/";
    static final String FILE_BG            = PATH + "background_mall.jpg";
    static final String FILE_ZOMBIE_LEFT   = PATH + "zombie_left.png";
    static final String FILE_ZOMBIE_RIGHT  = PATH + "zombie_right.png";
    static final String FILE_SQUASH        = PATH + "squash.gif";
    static final String FILE_HEART         = PATH + "heart.png";    
    
    static final String FILE_THEME         = PATH + "theme.wav";
    static final String FILE_SFX_GOOD      = PATH + "click.wav";
    static final String FILE_SFX_BAD       = PATH + "blob.wav";
    
    //----- Sizes and locations
    static final int FRAMEWIDTH    = 800;
    static final int FRAMEHEIGHT   = 550;
    static final int FLOOR_UP      = 170;
    static final int FLOOR_DOWN    = 370;
    
    static final int ZOMBIEWIDTH   = 70;
    static final int ZOMBIEHEIGHT  = 90;
    
    static final int ITEMWIDTH     = 60;
    static final int ITEMHEIGHT    = 60;
}


// Auxiliary class to resize image
class MyImageIcon extends ImageIcon
{
    public MyImageIcon(String fname)  { super(fname); }
    public MyImageIcon(Image image)   { super(image); }

    public MyImageIcon resize(int width, int height)
    {
	Image oldimg = this.getImage();
	Image newimg = oldimg.getScaledInstance(width, height, java.awt.Image.SCALE_DEFAULT);
        return new MyImageIcon(newimg);
    }
}


// Auxiliary class to play sound effect (support .wav or .mid file)
class MySoundEffect
{
    private Clip         clip;
    private FloatControl gainControl;         

    public MySoundEffect(String filename)
    {
	try
	{
            java.io.File file = new java.io.File(filename);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioStream);            
            gainControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
	}
	catch (Exception e) { e.printStackTrace(); }
    }
    public void playOnce()             { clip.setMicrosecondPosition(0); clip.start(); }
    public void playLoop()             { clip.loop(Clip.LOOP_CONTINUOUSLY); }
    public void stop()                 { clip.stop(); }
    public void setVolume(float gain)
    {
        if (gain < 0.0f)  gain = 0.0f;
        if (gain > 1.0f)  gain = 1.0f;
        float dB = (float)(Math.log(gain) / Math.log(10.0) * 20.0);
        gainControl.setValue(dB);
    }
}