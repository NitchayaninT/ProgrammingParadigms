package Ex8_6580081;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

class MainApplication extends JFrame //implements KeyListener
{
    private JLabel          contentpane;
    private CharacterLabel  []charLabels;
    private CharacterLabel  flyingLabel;
    private ItemLabel       itemLabel;
    
    private int framewidth   = MyConstants.FRAME_WIDTH;
    private int frameheight  = MyConstants.FRAME_HEIGHT;
    private int groundY      = MyConstants.GROUND_Y;
    private int skyY         = MyConstants.SKY_Y;
    private int chwidth      = MyConstants.CH_WIDTH;
    private int chheight     = MyConstants.CH_HEIGHT;
    private int itwidth      = MyConstants.IT_WIDTH;
    private int itheight     = MyConstants.IT_HEIGHT;

    public static void main(String[] args) 
    {
	new MainApplication();
    }	    
    
    public MainApplication()
    {      
	setSize(framewidth, frameheight); 
        setLocationRelativeTo(null);
	setVisible(true);
        setTitle("Wings off");
	setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );

        
	// set background image by using JLabel as contentpane
	setContentPane(contentpane = new JLabel());
	MyImageIcon background = new MyImageIcon(MyConstants.FILE_BG).resize(framewidth, frameheight);
	contentpane.setIcon( background );
	contentpane.setLayout( null );

        
        charLabels    = new CharacterLabel[2];
	charLabels[0] = new CharacterLabel(MyConstants.FILE_CH_1_MAIN, MyConstants.FILE_CH_1_ALT, 
                                           chwidth, chheight, this); 
        charLabels[0].setName("Marmite");        
        charLabels[0].setMoveConditions(200, groundY, true, false); 
        
        charLabels[1] = new CharacterLabel(MyConstants.FILE_CH_2_MAIN, MyConstants.FILE_CH_2_ALT, 
                                           chwidth, chheight, this); 
        charLabels[1].setName("Butter");        
        charLabels[1].setMoveConditions(500, groundY, true, false);
                    
        itemLabel = new ItemLabel(MyConstants.FILE_ITEM, itwidth, itheight, this);
        itemLabel.setMoveConditions(200, skyY, true, true);        

        
        // first added label is at the front, last added label is at the back
        contentpane.add( itemLabel );
        contentpane.add( charLabels[0] );
        contentpane.add( charLabels[1] );
        
	repaint();
    }
    
    public CharacterLabel[] getAllCharLabels() { return charLabels; }    
    public CharacterLabel   getFlyingLabel()   { return flyingLabel; }    
    public void setFlying(CharacterLabel cb)   { flyingLabel = cb; }
}

////////////////////////////////////////////////////////////////////////////////
abstract class BaseLabel extends JLabel
{
    protected String           name;
    protected MyImageIcon      iconMain, iconAlt;
    protected int              curX, curY, width, height;
    protected boolean          horizontalMove, verticalMove;
    protected MainApplication  parentFrame;   
    
    public BaseLabel() { }
    public BaseLabel(String file1, String file2, int w, int h, MainApplication pf)				
    { 
        width = w; height = h;
        iconMain = new MyImageIcon(file1).resize(width, height);  
	setIcon(iconMain);        
	setHorizontalAlignment(JLabel.CENTER);
        parentFrame = pf;  
        
        if (file2 != null) iconAlt = new MyImageIcon(file2).resize(width, height);
        else               iconAlt = null;
    }

    public void setName(String n)   { name = n; }
    public String getName()         { return name; }
    public void setMainIcon()       { setIcon(iconMain); }    
    public void setAltIcon()        { setIcon(iconAlt); }
    
    public void setMoveConditions(int x, int y, boolean hm, boolean vm)
    {
        curX = x; curY = y; 
        setBounds(curX, curY, width, height);
        setMoveConditions(hm, vm);
    } 
    public void setMoveConditions(boolean hm, boolean vm)
    {
        horizontalMove = hm; verticalMove = vm;
    }
    
    abstract public void updateLocation(); 
}

////////////////////////////////////////////////////////////////////////////////
class CharacterLabel extends BaseLabel //implements MouseListener
{ 
    public CharacterLabel(String file1, String file2, int w, int h, MainApplication pf)				
    { 
        super(file1, file2, w, h, pf);
    }
    
    public void updateLocation()    { }    
    public void moveUp()            { }
    public void moveDown()          { }
    public void moveLeft()          { }
    public void moveRight()         { }
}

////////////////////////////////////////////////////////////////////////////////
class ItemLabel extends BaseLabel //implements MouseMotionListener
{
    public ItemLabel(String file, int w, int h, MainApplication pf)				
    { 
        super(file, null, w, h, pf);
    }   

    public void updateLocation()    { }
}
