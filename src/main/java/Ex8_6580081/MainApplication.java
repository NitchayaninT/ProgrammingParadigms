//Pakin Panawattanakul 6580043
//Nitchayanin Thamkunanon 6580081
package Ex8_6580081;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

class MainApplication extends JFrame implements KeyListener
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

        //charLabels keep marmite and butter
        charLabels    = new CharacterLabel[2];
	    charLabels[0] = new CharacterLabel(MyConstants.FILE_CH_1_MAIN, MyConstants.FILE_CH_1_ALT,
                                           chwidth, chheight, this);  //marmite (alt = crow)
        charLabels[0].setName("Marmite");        
        charLabels[0].setMoveConditions(200, groundY, true, false); 
        
        charLabels[1] = new CharacterLabel(MyConstants.FILE_CH_2_MAIN, MyConstants.FILE_CH_2_ALT, 
                                           chwidth, chheight, this); //butter (alt = butterfly)
        charLabels[1].setName("Butter");        
        charLabels[1].setMoveConditions(500, groundY, true, false);
                    
        itemLabel = new ItemLabel(MyConstants.FILE_ITEM, itwidth, itheight, this);
        itemLabel.setMoveConditions(200, skyY, true, true);//wing

        // first added label is at the front, last added label is at the back
        contentpane.add( itemLabel ); //wing
        contentpane.add( charLabels[0] ); //marmite
        contentpane.add( charLabels[1] ); //butter

        addKeyListener(this);
        addMouseListener(charLabels[0]);
        addMouseListener(charLabels[1]);
        addMouseMotionListener(itemLabel);
	    repaint();
    }
    
    public CharacterLabel[] getAllCharLabels() { return charLabels; }    
    public CharacterLabel   getFlyingLabel()   { return flyingLabel; }    
    public void setFlying(CharacterLabel cb)   { flyingLabel = cb; }

    //Triggers after you press and then "release" a key but only if the key produces a character
    @Override
    public void keyTyped(KeyEvent e) {}

    //Triggers as soon as you press down any key (including non-text keys like arrow keys, shift, alt, ctrl, etc.).
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode){
            // move marmite by WASD
            case KeyEvent.VK_A : charLabels[0].moveLeft(); break;
            case KeyEvent.VK_D : charLabels[0].moveRight(); break;
            case KeyEvent.VK_W :
                if (charLabels[0].getFlyable()){
                    charLabels[0].moveUp();
                }
                break;
            case KeyEvent.VK_S :
                if (charLabels[0].getFlyable()){
                    charLabels[0].moveDown();
                }
                break;

            //move butter by arrow keys
            case KeyEvent.VK_LEFT : charLabels[1].moveLeft(); break;
            case KeyEvent.VK_RIGHT : charLabels[1].moveRight(); break;
            case KeyEvent.VK_UP :
                if (charLabels[1].getFlyable()){
                    charLabels[1].moveUp();
                }
                break;
            case KeyEvent.VK_DOWN :
                if (charLabels[1].getFlyable()){
                    charLabels[1].moveDown();
                }
                break;
            case KeyEvent.VK_ESCAPE :
                takeWingsOff();
        }

    }

    //Detecting when the user stops pressing a key.
    @Override
    public void keyReleased(KeyEvent e) {
    }

    public void takeWingsOff()
    {
        Random rand = new Random();
        int x = rand.nextInt(framewidth);
        int y = rand.nextInt(frameheight);
        if (charLabels[0].getFlyable()){
            CharacterLabel marmite = charLabels[0];
            marmite.switchCharacter();
            marmite.setMoveConditions(marmite.curX,this.getY()-marmite.getHeight(),true,false);
            itemLabel.setMoveConditions(x,y,true,true);
            itemLabel.setVisible(true);
        }
        else if (charLabels[1].getFlyable()){
            CharacterLabel butter = charLabels[1];
            butter.switchCharacter();
            butter.setMoveConditions(butter.curX,this.getY()-butter.getHeight(),true,false);
            itemLabel.setMoveConditions(x,y,true,true);
            itemLabel.setVisible(true);
        }
        setTitle("Wings off");
    }
}

////////////////////////////////////////////////////////////////////////////////
/// base class for each icon
abstract class BaseLabel extends JLabel
{
    protected String           name;
    protected MyImageIcon      iconMain, iconAlt;
    protected int              curX, curY, width, height; //label position
    protected boolean          horizontalMove, verticalMove;
    protected MainApplication  parentFrame;   
    
    public BaseLabel() { }
    public BaseLabel(String file1, String file2, int w, int h, MainApplication pf)	 //our label can switch between 2 modes (2 files,main and alt images)
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

    //for takeWingsOff, butter and marmite have to be on the ground
    public void setMoveConditions(int x, int y, boolean hm, boolean vm)
    {
        curX = x; curY = y; 
        setBounds(curX, curY, width, height);
        setMoveConditions(hm, vm);
    }
    //for setting move condition, if the icon can move vertical, horizontal, both or neither
    public void setMoveConditions(boolean hm, boolean vm)
    {
        horizontalMove = hm; verticalMove = vm;
    }
    
    abstract public void updateLocation(); 
}

////////////////////////////////////////////////////////////////////////////////
//characterLabel : for Marmite and Butter
//Marmite can move left/right by keys A/D, but can't move up/down
//Butter can move left/right by arrow keys LEFT/RIGHT, but can't move up/down
class CharacterLabel extends BaseLabel implements MouseListener
{ 
    public CharacterLabel(String file1, String file2, int w, int h, MainApplication pf)				
    { 
        super(file1, file2, w, h, pf);
    }

    public void updateLocation()    {setBounds(curX,curY,width,height);}
    public void moveUp(){
        Container p = parentFrame.getContentPane();
        System.out.println("height"+getHeight()+" y= "+curY);
        //when reach top, it wont appear on other side
        if(curY > 0) {curY -=10;}
        updateLocation();
    }
    public void moveDown(){
        Container p = parentFrame.getContentPane();
        //when reach bottom, it wont appear on other side
        if(curY < p.getHeight()-getHeight()) {curY +=10;}
        updateLocation();
    }
    public void moveLeft(){
        Container p = getParent();
        curX -= 10;
        //when reach left side of the frame, it will reappear on right side (enters on right side)
        if(curX+width<0){ //when icon "completely" gone
            curX = p.getWidth();
        }
        updateLocation();
    }
    public void moveRight(){
        Container p = getParent();
        curX += 10;
        //when reach right side of the frame, it will reappear on left side (enters on left side)
        if(curX>p.getWidth()){
            curX = -width;
        }
        updateLocation();
    }
    //Switch between the character with & without wings.
    public void switchCharacter()
    {
        if(getIcon()==iconMain) {setAltIcon(); }
        else {setMainIcon();}
    }
    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    //Jumps to a random location whenever the mouse enters the label’s bounds.(if has wing)
    @Override
    public void mouseEntered(MouseEvent e) {
        if(verticalMove){
            Container p = parentFrame.getContentPane();
            Random random = new Random();
            if (p != null) {
                int maxX = p.getWidth() - width;
                int maxY = p.getHeight() - height;

                curX = random.nextInt(maxX);
                curY = random.nextInt(maxY);

                updateLocation();
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {}

    public boolean getFlyable(){
        return this.verticalMove;
    }
}

////////////////////////////////////////////////////////////////////////////////
/// wings (itemLabel)
class ItemLabel extends BaseLabel implements MouseMotionListener //because you need to drag item around
{
    public ItemLabel(String file, int w, int h, MainApplication pf)				
    { 
        super(file, null, w, h, pf);
    }   

    public void updateLocation()    {setBounds(curX,curY,width,height);}

    @Override
    public void mouseDragged(MouseEvent e) {
        //previous location plus x and y locations
        System.out.println(e.getX());
        curX = e.getX() - getHeight();
        curY = e.getY() - getWidth();

        //update the location to follow the cursor
        Container p = getParent();
        if (curX < 0)  curX = 0; //dragged within the frame
        if (curY < 0)  curY = 0; //dragged within the frame
        if (curX + width  > p.getWidth())   curX = p.getWidth() - width; //move within the bounds
        if (curY + height > p.getHeight())  curY = p.getHeight() - height;

        //if overlaps with CharacterLabel(which is a content in the frame), switch its and character's labels to alternative labels
        for (Component c : parentFrame.getContentPane().getComponents()) {
            if (c instanceof CharacterLabel) {
                checkCollision((CharacterLabel) c);
            }
        }
        updateLocation();
    }

    @Override
    public void mouseMoved(MouseEvent e) {}

//method to check collision between wing and any icon (needs to check which icon it collides first)
    public void checkCollision(CharacterLabel other)
    {
        Container p = getParent();
        if ( this.getBounds().intersects(other.getBounds()) )
        {
            other.switchCharacter(); //transform to ALT icon
            other.setMoveConditions(true,true);
            this.setMoveConditions(false,false);
            this.setVisible(false); //hide wings
            parentFrame.setTitle("Wings on "+other.getName());
        }
    }
}
