import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Radar extends JFrame implements ActionListener, ListSelectionListener 
{
    private JButton enterAirspace;  // As it says
    private JButton leaveAirspace;  // As it says
    private JButton listPassengers; // As it says
    private JButton quit;        	// As it says
    private JTextArea info;
    private JList list;
    private DefaultListModel listModel;
    private int index;
    int size = 0;
	public Radar()
	{
		  // Configure the window
	      setTitle("Radar/Transceiver");
	      setLocation(600,600);
	      setSize(500,350);
	      setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	      Container window = getContentPane();
	      window.setLayout(new FlowLayout());     // The default is that JFrame uses BorderLayout 
	      // Set up input GUI
	      enterAirspace = new JButton("Enter Airspace");
	      window.add(enterAirspace);
	      enterAirspace.addActionListener(this);
	      
	      leaveAirspace = new JButton("Leave Airspace");
	      window.add(leaveAirspace);
	      leaveAirspace.addActionListener(this);
	      
	      listPassengers = new JButton("List Passengers");
	      window.add(listPassengers);
	      listPassengers.addActionListener(this);
	      
	      quit = new JButton("Quit");
	      window.add(quit);
	      quit.addActionListener(this);
	      
	      info = new JTextArea();
	      info.setWrapStyleWord(true);
	      info.setLineWrap(true); //Now we will only scroll vertical
	      info.setSize(350, 200);
	      info.setRows(16);
	      JScrollPane pane = new JScrollPane(info);
	      info.setEditable(false);
	      add(pane, BorderLayout.CENTER);
	      
	      listModel = new DefaultListModel();
	      listModel.addElement("Jane Doe");
	      listModel.addElement("John Smith");
	      listModel.addElement("Kathy Green");
	      //Create the list and put it in a scroll pane.
	      list = new JList(listModel);
	      list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	      list.setSelectedIndex(-1);
	      list.addListSelectionListener(this);
	      list.setVisibleRowCount(10);
	      
	      size = listModel.getSize();
	      listPassengers.setEnabled(false);
	      leaveAirspace.setEnabled(false);
	      JScrollPane listScrollPane = new JScrollPane(list);
	      listScrollPane.setPreferredSize(new Dimension(100, 200));
	      add(listScrollPane, BorderLayout.CENTER);
	      setVisible(true);
	 }
	
	 public void actionPerformed(ActionEvent e) 
	 {
		 index = list.getSelectedIndex();	
		 size = listModel.getSize(); 
		 if (e.getSource() == enterAirspace)
			 	inboundAircraft();
		 if (e.getSource() == leaveAirspace)
	            outboundAircraft();
		 if (e.getSource() == listPassengers)
	            getPassengerList();
		 if (e.getSource() == quit)
	            System.exit(0);	  
		 	 
		 list.setSelectedIndex(index);
	     list.ensureIndexIsVisible(index);
	 } // actionPerformed
	 public void inboundAircraft()
	 {
		 
	 }
	 public void outboundAircraft()
	 {
		 listModel.remove(index);
	 }
	 public void getPassengerList()
	 {
		 JOptionPane.showMessageDialog(this, "Index selected is " + index);
	 }
	 
	 public void createAircraft()
	 {
		 
	 }

	@Override
	public void valueChanged(ListSelectionEvent e) 
	{
		 if(size == 0 || list.getSelectedIndex() == -1)
		 {
			 listPassengers.setEnabled(false);
			 leaveAirspace.setEnabled(false);
		 }
		 else
		 {
			 listPassengers.setEnabled(true);
			 leaveAirspace.setEnabled(true);
		 }
	}
}
