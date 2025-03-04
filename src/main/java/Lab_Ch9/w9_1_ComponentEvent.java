package Lab_Ch9;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// ----- (1) GUI structure & programming concept
class w9_1_ComponentEvent extends JFrame 
{
    private JPanel	     contentpane;
    private JButton          button1, button2;
    private JComboBox        combo;
    private JToggleButton [] tb;
    private ButtonGroup      bgroup;

    public w9_1_ComponentEvent()
    {
	    setTitle("This is a Frame");
        setSize(500, 150);
        setLocationRelativeTo(null); //make frame appear middle of the screen
        setVisible(true);
        setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );

        contentpane = (JPanel)getContentPane();
        contentpane.setBackground( Color.GRAY );
        contentpane.setLayout( new FlowLayout() );

        AddComponents(); //add components to the frame -> have GUI like what we did in ch8
        AddListeners(); //handle the events
    }


    public void AddComponents()
    {
	String [] items = new String[10];
	for (int i=0; i < 10; i++) items[i] = " --- item " + i + " ---";

	button1 = new JButton("Click First");
	button2 = new JButton("Click Second");

	combo = new JComboBox( items );

	bgroup  = new ButtonGroup();
	tb      = new JToggleButton[3];
	JPanel bpanel = new JPanel();
	for (int i=0; i < 3; i++) 
	{ 
            tb[i] = new JRadioButton( items[i] );
            bgroup.add( tb[i] );
            bpanel.add( tb[i] );
	}

	contentpane.add(button1);
	contentpane.add(button2);
	contentpane.add(combo);
	contentpane.add(bpanel);

	validate();
    }

//to interact with user
    public void AddListeners()
    {
	// ----- (2) add ComponentListener to frame object
	    this.addComponentListener( new MyComponentListener() );

	// ----- (3) add FocusListener to buttons and combo box

        button1.addFocusListener( new MyFocusListener() );
        button2.addFocusListener( new MyFocusListener() );
        combo.addFocusListener( new MyFocusListener() );
    }

    public static void main(String[] args) 
    {
	    new w9_1_ComponentEvent();
    } //in main method, just create the frame object. hide all the instructions inside the frame object
}

////////////////////////////////////////////////////////////////////////////////////
class MyComponentListener extends ComponentAdapter //overrides only 2 handler methods out of 4
{
    @Override
    public void componentMoved( ComponentEvent e )
    {
	System.out.println( e.paramString() );
    }

    @Override
    public void componentResized( ComponentEvent e )
    {
	System.out.println( e.paramString() );
    }
};


class MyFocusListener extends FocusAdapter
{
    @Override
    public void focusLost( FocusEvent e )
    {
        String s;
        if ( e.isTemporary() ) s = "temporary focus";
        else s = "permanent focus";

        if (e.getComponent() instanceof JButton)  //e = focus event object.
        {
                JButton source = (JButton)e.getComponent();
                System.out.println( "<< " + source.getText() + " >>  loses " + s );
        }
        else if (e.getComponent() instanceof JComboBox)
        {
                JComboBox source = (JComboBox)e.getComponent();
                System.out.println( source.getSelectedItem().toString() + "  loses " + s);
        }
    }

    @Override
    public void focusGained( FocusEvent e )
    {
	String s;
	if ( e.isTemporary() ) s = "temporary focus";
	else s = "permanent focus";

	if (e.getComponent() instanceof JButton) 
	{
            JButton source = (JButton)e.getComponent();
            System.out.println( "<< " + source.getText() + " >>  gains " + s );
	}
	else if (e.getComponent() instanceof JComboBox)
	{
            JComboBox source = (JComboBox)e.getComponent();
            System.out.println( source.getSelectedItem().toString() + "  gains " + s);
        }
    }
};