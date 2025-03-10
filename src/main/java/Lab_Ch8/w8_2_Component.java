package Lab_Ch8;

import java.awt.*;
import javax.swing.*;

//better program structure for event driven programming
class w8_2_Component extends JFrame
{
    public w8_2_Component()
    {
		this.setTitle("This is a Frame");
		this.setBounds(200, 200, 600, 500);
			//this.setSize(600, 500); this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setResizable(true);
		this.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );


		// ----- (1) setup layout
		JPanel contentpane = (JPanel)getContentPane(); //gets content pane
		contentpane.setLayout( new BorderLayout(25, 25) ); //set layout manager


		// ----- (2) setup components
		String [] items = new String[10];
		for (int i=0; i < 10; i++) items[i] = " --- item " + i + " ---";

		JLabel  label   = new JLabel("Label in the North");
		label.setFont(new Font("Monospaced", Font.PLAIN, 20));

		JButton button1 = new JButton("Click South A");
		JButton button2 = new JButton("Click South B");//dont see buttons yet

		JPanel cregion = new JPanel();
		cregion.setBackground( Color.CYAN );


		// ----- (3) add components to container
		//           notice 2 buttons in the south
		contentpane.add(label, BorderLayout.NORTH);
		contentpane.add(cregion, BorderLayout.CENTER);
		//contentpane.add(button1, BorderLayout.SOUTH);
		//contentpane.add(button2, BorderLayout.SOUTH); //button 2 placed on top of button 1

		//what if want to add both?
		JPanel southPanel = new JPanel();
		southPanel.add(button1);
		southPanel.add(button2);
		contentpane.add(southPanel, BorderLayout.SOUTH);


		// ----- (5) set cursor to other formats
		button1.setCursor( new Cursor(Cursor.MOVE_CURSOR) );


		// ----- (6) add text field, password field, text area

		JTextField tf = new JTextField("Initial Text", 20);
		tf.setFont(new Font("Comic Sans MS", Font.BOLD+Font.ITALIC, 20));
		cregion.add(tf);

		JPasswordField pf = new JPasswordField("password", 10);
		pf.setEchoChar('*');
		cregion.add(pf);

		JTextArea ta = new JTextArea(5, 20);
		cregion.add(ta);
		//text area with scrollbar
		JScrollPane tsc = new JScrollPane(ta);
		tsc.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		cregion.add(tsc);



		// ----- (7) add check box, radio button
			//           try different GridLayout

		JToggleButton [] tb = new JToggleButton[5];
		tb[0] = new JCheckBox( items[0], true );
		tb[1] = new JCheckBox( items[1], true );
		tb[2] = new JRadioButton( items[2] );
		tb[3] = new JRadioButton( items[3], true );
		tb[4] = new JRadioButton( items[4], true );

			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout( new GridLayout(2, 3) );
		for (int i=0; i < 5; i++) buttonPanel.add( tb[i] );
		cregion.add(buttonPanel);



		// ----- (8) group multiple radio buttons

		ButtonGroup rgroup = new ButtonGroup();
		for (int i=2; i < 5; i++) rgroup.add( tb[i] );


		// ----- (9) add list box, combo box

		JComboBox cb = new JComboBox(items); //combo box
		cregion.add(cb);

		JList lb = new JList(items);//list box
		lb.setVisibleRowCount(5);
			JScrollPane lsc = new JScrollPane(lb);//put list in scroll pane
			lsc.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		cregion.add(lsc);

		//lb.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		//lb.setSelectionMode( ListSelectionModel.SINGLE_INTERVAL_SELECTION );
			lb.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION ); //can select many items


		// ----- (4) validate container once all components are added
		this.validate();
    }

    public static void main(String[] args) 
    {
		new w8_2_Component();//to create frame object
    }
}
