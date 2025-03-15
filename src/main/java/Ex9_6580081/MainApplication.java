//Pakin Panawattanakul 6580043
//Nitchayanin Thamkunanon 6580081
package Ex9_6580081;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class MainApplication extends JFrame 
{
    // components
    private JPanel           contentpane;
    private JLabel           drawpane;
    private JComboBox        combo;
    private JToggleButton    []tb;
    private ButtonGroup      bgroup;
    private JButton          moveButton, stopButton, squashButton, heartButton;
    private JTextField       scoreText;
    private MyImageIcon      backgroundImg;    
    private MySoundEffect    themeSound;
    
    private ZombieLabel      zombieLabel;
    private MainApplication  currentFrame;

    private int framewidth  = MyConstants.FRAMEWIDTH;
    private int frameheight = MyConstants.FRAMEHEIGHT;
    private int score;

    public static void main(String[] args) 
    {
        new MainApplication();
    }    

    //--------------------------------------------------------------------------
    public MainApplication()
    {   
        setTitle("Zombie Game");
	setSize(framewidth, frameheight); 
        setLocationRelativeTo(null);
	setVisible(true);
	setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
        currentFrame = this;

        // (1) Add WindowListener (anonymous class)
        //     - Stop everything. Show total score


	contentpane = (JPanel)getContentPane();
	contentpane.setLayout( new BorderLayout() );        
        AddComponents();
    } 
    
    //--------------------------------------------------------------------------
    public void AddComponents()
    {        
	backgroundImg  = new MyImageIcon(MyConstants.FILE_BG).resize(framewidth, frameheight-80);
	drawpane = new JLabel();
	drawpane.setIcon(backgroundImg);
        drawpane.setLayout(null);

	themeSound = new MySoundEffect(MyConstants.FILE_THEME); 
        themeSound.playLoop(); themeSound.setVolume(0.4f);
        
        zombieLabel = new ZombieLabel(currentFrame);
        drawpane.add(zombieLabel);
        
        
        // (2) Add ActionListener (anonymous class) to moveButton
        //     - If Zombie isn't moving, create zombieThread to make it move
	moveButton = new JButton("Move");
            
        
        // (3) Add ActionListener (anonymous class) to stopButton
        //     - Stop zombieThread, i.e. make it return from method run
	stopButton = new JButton("Stop");
   
        
	// (4) Add ItemListener (anonymouse class) to combo 
        //     - Set Zombie's speed, i.e. sleeping time for zombieThread
        String[] speed = { "fast", "medium", "slow"};
        combo = new JComboBox(speed);
	combo.setSelectedIndex(1);

        
	// (5) Add ItemListener (anonymouse class) to tb[i]
        //     - Make Zombie turn left/right
        //     - Only 1 of them can be selected at a time
        tb = new JToggleButton[2];
        bgroup = new ButtonGroup();      
        tb[0] = new JRadioButton("Left");   tb[0].setName("Left");
        tb[1] = new JRadioButton("Right");  tb[1].setName("Right"); 
	tb[1].setSelected(true);
  
        
        // (6) Add ActionListener (anonymous class) to squashButton
        //     - Create a new itemThread with Squash label
	squashButton = new JButton("Squash");


        // (7) Add ActionListener (anonymous class) to heartButton
        //     - Create a new itemThread with Heart label
	heartButton = new JButton("Heart");
     

	scoreText = new JTextField("0", 5);		
	scoreText.setEditable(false);

        JPanel control  = new JPanel();
        control.setBounds(0,0,1000,50);
	control.add(new JLabel("Zombie : "));
        control.add(moveButton);
        control.add(stopButton);
        control.add(combo);
        control.add(tb[0]);
        control.add(tb[1]);
	control.add(new JLabel("             "));
	control.add(squashButton);
        control.add(heartButton);
	control.add(new JLabel("             "));
	control.add(new JLabel("Score : "));
	control.add(scoreText);
        contentpane.add(control, BorderLayout.NORTH);
        contentpane.add(drawpane, BorderLayout.CENTER);     
        
        validate();       
    }    

    //--------------------------------------------------------------------------
    public void setZombieThread()
    {
        Thread zombieThread = new Thread() {
            public void run()
            {
                while (zombieLabel.isMove())
                {
                    zombieLabel.updateLocation();
                }          
            } // end run
        }; // end thread creation
        zombieThread.start();
    }
    //--------------------------------------------------------------------------
    public void setItemThread(int type)
    {
        Thread itemThread = new Thread() {
            public void run()
            {
                // (8) Create a new ItemLabel with type 0 (Squash) or 1 (Heart), 
                //     add it to drawpane, and do the following tasks in loop:
                //     - Update item location 
                //
                //     - Check whether it collides with Zombie. If it does, play 
                //       hit sound and update score
                //
                //     - If Squash hits Zombie that stands/walks on upper floor, 
                //       push Zombie down to lower floor
                //
                //     - Once reaching the top/bottom or colliding with Zombie, 
                //       remove item from drawpane and end this thread
                
            } // end run
        }; // end thread creation
        itemThread.start();
    }
    //--------------------------------------------------------------------------
    public void updateScore(int hp)
    {
        // (9) Score update must be synchronized since it can be done by >1 itemThreads

    }  
    
} // end class MainApplication


////////////////////////////////////////////////////////////////////////////////
class ZombieLabel extends JLabel
{
    private MainApplication  parentFrame;   
    private MyImageIcon      leftImg, rightImg;      
        
    private int width     = MyConstants.ZOMBIEWIDTH;
    private int height    = MyConstants.ZOMBIEHEIGHT;
    private int floorUp   = MyConstants.FLOOR_UP;
    private int floorDown = MyConstants.FLOOR_DOWN;
    private int curY      = floorUp;
    private int curX      = 300;
    private int speed     = 500;
    private boolean right = true, move = false;        
        
    public ZombieLabel(MainApplication pf)
    {
        parentFrame = pf;
            
        leftImg  = new MyImageIcon(MyConstants.FILE_ZOMBIE_LEFT).resize(width, height);
        rightImg = new MyImageIcon(MyConstants.FILE_ZOMBIE_RIGHT).resize(width, height);
        setIcon(rightImg);
        setBounds(curX, curY, width, height);
    }
        
    public void setSpeed(int s)     { speed = s; }
    public void turnLeft()          { setIcon(leftImg);  right = false; }
    public void turnRight()         { setIcon(rightImg); right = true; }
    public void setMove(boolean m)  { move = m; }
    public boolean isMove()         { return move; }
        
    // (10) Add code to switch between upper and lower floor when Zombie reaches 
    //      left/right border of the frame
    public void updateLocation()
    {
        if (!right)
        {   
            curX = curX - 50;
            if (curX < -80) 
            { 
                curX = parentFrame.getWidth(); 
            } 			
        }
        else
        {
            curX = curX + 50;
            if (curX > parentFrame.getWidth()) 
            { 
                curX = -40; 
            }			
        }
        setLocation(curX, curY);
        repaint();             
        try { Thread.sleep(speed); } 
        catch (InterruptedException e) { e.printStackTrace(); }            
    } 
    
} // end class ZombieLabel

////////////////////////////////////////////////////////////////////////////////
class ItemLabel extends JLabel 
{
    private MainApplication  parentFrame;   
    
    private int            type;      // 0 = bad item (go down), 1 = good item (go up)
    private MyImageIcon    itemImg;
    private MySoundEffect  hitSound;
    
    String [] imageFiles = { MyConstants.FILE_SQUASH, MyConstants.FILE_HEART };        
    String [] soundFiles = { MyConstants.FILE_SFX_BAD,MyConstants.FILE_SFX_GOOD };
    int    [] hitpoints  = { -1, 1 };

    private int width    = MyConstants.ITEMWIDTH;
    private int height   = MyConstants.ITEMHEIGHT;
    private int curX, curY;
    private int speed = 600;

    public ItemLabel(MainApplication pf, int t)
    {
        parentFrame = pf; type = t;
        
        Random rand = new Random();
        curX = rand.nextInt(10, parentFrame.getWidth()-100);
        if (type == 0)  curY = 0; 
        else            curY = parentFrame.getHeight()-height; 
          
        itemImg  = new MyImageIcon(imageFiles[type]).resize(width, height);
        hitSound = new MySoundEffect(soundFiles[type]);
        setIcon(itemImg);
        setBounds(curX, curY, width, height);
    }
        
    public int  getType()              { return type; }
    public void playHitSound()         { hitSound.playOnce(); }
    public int  getHitPoints()         { return hitpoints[type]; }
        
} // end class ItemLabel