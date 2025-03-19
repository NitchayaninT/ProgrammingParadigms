//Pakin Panawattanakul 6580043
//Nitchayanin Thamkunanon 6580081
package Ex9_6580081;

import java.sql.SQLOutput;
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
        currentFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                //show total score. first parameter is current frame for dialog to attach to
                JOptionPane.showMessageDialog(currentFrame, "Game Over!\nYour total score: " + score, "Game Over", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        });

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
        moveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zombieLabel.setMove(true);
                setZombieThread();
            }
        });
        
        // (3) Add ActionListener (anonymous class) to stopButton
        //     - Stop zombieThread, i.e. make it return from method run
	    stopButton = new JButton("Stop");
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zombieLabel.setMove(false);
            }
        });
        
	// (4) Add ItemListener (anonymouse class) to combo 
        //     - Set Zombie's speed, i.e. sleeping time for zombieThread
        String[] speed = { "fast", "medium", "slow"};
        combo = new JComboBox(speed);
	    combo.setSelectedIndex(1);
        combo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String selected = (String) e.getItem();
                int sleepTime = 10000;
                switch (selected){
                    case "fast" :
                        sleepTime = 250;
                        break;

                    case "medium" :
                        sleepTime = 500;
                        break;

                    case "slow" :
                        sleepTime = 1000;
                        break;
                    default:
                        System.err.println("Error @speed comboBox");
                        System.exit(-1);
                }
                zombieLabel.setSpeed(sleepTime);
            }
        });


	// (5) Add ItemListener (anonymouse class) to tb[i]
        //     - Make Zombie turn left/right
        //     - Only 1 of them can be selected at a time
        tb = new JToggleButton[2];
        bgroup = new ButtonGroup();
        tb[0] = new JRadioButton("Left");   tb[0].setName("Left");
        tb[1] = new JRadioButton("Right");  tb[1].setName("Right");
	    tb[1].setSelected(true);

        // add the Radio buttons to the group to make it selected one @ a time
        bgroup.add(tb[0]);
        bgroup.add(tb[1]);
        for(int i=0;i<tb.length;i++)
        {
            tb[i].addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if(tb[0].isSelected())
                        zombieLabel.turnLeft();
                    else
                        zombieLabel.turnRight();
                }
            });
        }

        
        // (6) Add ActionListener (anonymous class) to squashButton
        //     - Create a new itemThread with Squash label
	    squashButton = new JButton("Squash");
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setItemThread(0);
            }
        });

        // (7) Add ActionListener (anonymous class) to heartButton
        //     - Create a new itemThread with Heart label
	    heartButton = new JButton("Heart");
        heartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setItemThread(1);
            }
        });

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
                while(zombieLabel.isMove()) {
                    zombieLabel.updateLocation();
                }
                System.out.println("ZombieThread running");
            } // end run
        }; // end thread creation
        zombieThread.start();
    }
    //--------------------------------------------------------------------------
    public void setItemThread(int type)
    {
        Thread itemThread = new Thread() {
            //if type squash, hp -1. if type heart, hp +1

            public void run()
            {

                boolean hit = false;
                ItemLabel item = new ItemLabel(currentFrame,type);
                drawpane.add(item);
                item.setVisible(true);
                int Yaxis = 0; //initialize
                while(!hit)
                {
                    Random rand = new Random();
                    int Xaxis =  rand.nextInt(contentpane.getWidth() - MyConstants.ITEMWIDTH);
                    //Update item location
                    switch(type){
                        case 0: //for squash
                            item.setBounds(Xaxis,Yaxis,MyConstants.ITEMWIDTH,MyConstants.ITEMHEIGHT);
                            Yaxis += 10;
                            break;
                        case 1: //for heart
                            Yaxis = contentpane.getHeight();
                            item.setBounds(Xaxis,Yaxis,MyConstants.ITEMWIDTH,MyConstants.ITEMHEIGHT);
                            Yaxis -= 10;
                            break;
                    }
                    System.out.println("Item Position: " + Xaxis + ", " + Yaxis);

                    // Check whether it collides with Zombie. If it does, play
                    // hit sound and update score
                    boolean collide = checkCollision(zombieLabel, item);
                    //If Squash hits Zombie that stands/walks on upper floor,
                    //push Zombie down to lower floor
                    if(collide)
                    {
                        if(zombieLabel.getY() == MyConstants.FLOOR_UP)
                        {
                            zombieLabel.setBounds(zombieLabel.getX(),MyConstants.FLOOR_DOWN,MyConstants.ZOMBIEWIDTH,MyConstants.ZOMBIEHEIGHT);
                        }
                        //Once colliding with Zombie,
                        //remove item from drawpane and end this thread
                        hit = true;
                        drawpane.remove(item);

                    }
                    else
                    {
                        //Once reaching the top/bottom ,
                        //remove item from drawpane and end this thread
                        switch(type){
                            case 0: //reach bottom
                                if(item.getY()==drawpane.getY())
                                {
                                    System.out.println("here");
                                    drawpane.remove(item);
                                    hit = true;
                                }
                                break;
                            case 1: //reach top
                                if(item.getY()==0)
                                {
                                    System.out.println("here2");
                                    drawpane.remove(item);
                                    hit = true;
                                }
                                break;
                        }
                    }
                    try {
                        Thread.sleep(30); // Smooth movement
                    } catch (InterruptedException e) {}
                }
                //update score
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
    // (9) Score update must be synchronized since it can be done by >1 itemThreads
    public synchronized void updateScore(int hp)
    {
        score += hp;
        System.out.println("Updated Score: " + score);
    }
    //method to check collision
    public boolean checkCollision(ZombieLabel other, ItemLabel item) {
        //zombie Label collies with ItemLabel
        if (item.getBounds().intersects(other.getBounds())) {
            //play hit sounds and update score
            item.playHitSound();
            updateScore(item.getHitPoints());
            return true; //collides!
        }
        else return false;
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
        System.out.println("2");
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