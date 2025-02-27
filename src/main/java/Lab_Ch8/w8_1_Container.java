package Lab_Ch8;

import java.awt.*;
import javax.swing.*;

class w8_1_Container
{
    public static void main(String[] args) 
    {
        JFrame frame = new JFrame("This is a Frame");

        // ----- (1) Minimum properties that must be set
        frame.setBounds(200, 200, 600, 300); //make frame appears on the screen first
        frame.setVisible(true);


	// ----- (2) Other properties
	//           - which one doesn't work ?
	//           - notice the difference between EXIT_ON_CLOSE and DISPOSE_ON_CLOSE

        frame.setMaximizedBounds( new Rectangle(800, 500) );
        frame.setResizable(true);
        //frame.getContentPane().setBackground( new Color(100, 150, 250) ); //change backround color of "content pane"
        //useless to just change color of the frame cuz its behind content pane (top level container)
        frame.getContentPane().setBackground( Color.PINK );
        frame.setCursor( new Cursor(Cursor.WAIT_CURSOR) ); //set cursor to be wait cursor
        frame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );

        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add( new JTextField("type anything", 20) );//added text after the frame already apeared on the screen

        
        // ----- (1.1) If the frame is visible before components are added,
        //             must call validate or pack to trigger layout manager & update screen
        frame.validate();
        //frame.pack();
        
        // ----- (1.2) Or add components first and make frame (with components) visible
        //             Don't need to call validate or pack 
        //frame.setVisible(true); //paints the frame plus many components
	


	// ----- (3) Dialog
	//           - notice the difference between dialog with and without owner
	//           - notice the difference between modal and non-modal dialogs

	JDialog dialog = new JDialog(frame);//frame is the owner
	dialog.setTitle("This is a Dialog");
	dialog.setModal(true);//non modal. you can still work with the frame once dialog appears(like help dialog)
        //if true, user cannot interact with the frame (main app) once dialog appears. use when force user to do task in dialog first(must have OWNER!!)
	dialog.setBounds(400, 400, 300, 150 );
	dialog.setVisible(true);//in order to make it appear on screen
	dialog.setDefaultCloseOperation( WindowConstants.DISPOSE_ON_CLOSE );



	// ----- (4) Quick dialog 
        //           - notice the difference between dialog with and without owner
	//no owner
	int op = JOptionPane.showConfirmDialog( null, 
		 "Press the button", "This is a Confirm Dialog", 
	         JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE );//no owner, appears at middle of screen

    //frame as owner
	JOptionPane.showMessageDialog( frame, 
		 ("Option  =  " + op), "This is a Message Dialog", 
		 JOptionPane.INFORMATION_MESSAGE ); //frame as owner = appears at the center of the frame

    }
}
