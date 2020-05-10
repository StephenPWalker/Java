import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * An interface to SAAMS: Maintenance Inspector Screen: Inputs events from the
 * Maintenance Inspector, and displays aircraft information. This class is a
 * controller for the AircraftManagementDatabase: sending it messages to change
 * the aircraft status information. This class also registers as an observer of
 * the AircraftManagementDatabase, and is notified whenever any change occurs in
 * that <<model>> element. See written documentation.
 * 
 * @stereotype boundary/view/controller
 * @url element://model:project::SAAMS/design:node:::id4tg7xcko4qme4cko4swuu.node146
 * @url element://model:project::SAAMS/design:view:::id15rnfcko4qme4cko4swib
 * @url element://model:project::SAAMS/design:view:::id4tg7xcko4qme4cko4swuu
 * @url element://model:project::SAAMS/design:node:::id15rnfcko4qme4cko4swib.node107
 * @url element://model:project::SAAMS/design:view:::id3y5z3cko4qme4cko4sw81
 */
public class MaintenanceInspector extends JFrame implements ActionListener, Observer, ListSelectionListener {
	/**
	 * The Maintenance Inspector Screen interface has access to the
	 * AircraftManagementDatabase.
	 * 
	 * @clientCardinality 1
	 * @supplierCardinality 1
	 * @label accesses/observes
	 * @directed
	 */

	//Initialise variables
	private AircraftManagementDatabase aircraftManagementDatabase;
	private int selectedIndex;
	int size = 0;

	private JLabel FlightCode;
	private JLabel FlightStatus;

	private JTextArea info; // As it says
	private JList <String>list; // As List of aircraft
	private DefaultListModel <String>listModel;

	private JButton FaultFound;
	private JButton FaultyAwaitingCleaning;
	private JButton OKAwaitCleaning;
	private JButton OKCheck;
	private JButton Repair;
	public MaintenanceInspector(AircraftManagementDatabase aircraftManagementDatabase) {

		this.aircraftManagementDatabase = aircraftManagementDatabase;
		
		//setup Java Swing frame
		setTitle("Maintenance Insprector Conroller");
		setLocation(40, 400);
		setSize(400, 350);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		Container window = getContentPane();
		window.setLayout(new FlowLayout());

		info = new JTextArea();
	    JScrollPane pane = new JScrollPane(info);
	    pane.setPreferredSize(new Dimension(280, 200));
	    info.setEditable(false);
	    add(pane, BorderLayout.CENTER);

		listModel = new DefaultListModel();
		
		// Create the list and put it in a scroll pane.
		list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(-1);
		list.addListSelectionListener(this);
		list.setVisibleRowCount(10);

		JScrollPane listScrollPane = new JScrollPane(list);
		listScrollPane.setPreferredSize(new Dimension(70, 200));
		add(listScrollPane, BorderLayout.CENTER);
		setVisible(true);


		//button for entering fault
		FaultFound = new JButton("Fault Found");
		window.add(FaultFound);
		FaultFound.addActionListener(this);
		
		//button for entering awaiting cleaning state after fault found
		FaultyAwaitingCleaning = new JButton("Faulty Awaiting Cleaning");
		window.add(FaultyAwaitingCleaning);
		FaultyAwaitingCleaning.addActionListener(this);
		
		//button for entering awaiting cleaning state
		OKAwaitCleaning = new JButton("Awaiting Cleaning OK");
		window.add(OKAwaitCleaning);
		OKAwaitCleaning.addActionListener(this);

		//button for notifying that all checks have been done
		OKCheck = new JButton("Check OK");
		window.add(OKCheck);
		OKCheck.addActionListener(this);
		

		//button for repair
		Repair = new JButton("Repair");
		window.add(Repair);
		Repair.addActionListener(this);

		//label for flight code
		FlightCode = new JLabel("[FLIGHT CODE]", SwingConstants.CENTER);
		window.add(FlightCode);

		//label for flight status
		FlightStatus = new JLabel("[FLIGHT STATUS]", SwingConstants.CENTER);
		window.add(FlightStatus);
		
		//set buttons to false for error handling 
		FaultFound.setEnabled(false);
		FaultyAwaitingCleaning.setEnabled(false);
		OKAwaitCleaning.setEnabled(false);
		OKCheck.setEnabled(false);
		Repair.setEnabled(false);
		
		// Display the frame
		setVisible(true);
		setResizable(false);
		
		// Subscribe to the model
		aircraftManagementDatabase.addObserver(this);
	}
	
	/*
	 * Changes the labels based on the aircraft selected from the list in order to
	 *  provide basic information
	 */
	private void setLabels(int valueSelected)
	{
		if (valueSelected != -1) {
			FlightCode.setText(aircraftManagementDatabase.getFlightCode(valueSelected));
			FlightStatus.setText(aircraftManagementDatabase.getStatusName(valueSelected));
		}
		else
		{
		FlightCode.setText("[FLIGHT CODE]");
		FlightStatus.setText("[FLIGHT STATUS]");
		}
	}

	/* 
	 * Receives and sends updates to and from the database regards
	 *  the aircrafts which enter the status for maintenance, and adds them to the list
	 *  */
	@Override
	public void update(Observable o, Object arg) {
		checkValue();
		String[] flightCodes = new String[size];
		for (int i = 0; i < size; i++) {
			flightCodes[i] = listModel.elementAt(i).toString();
		}
		String[] DBFlightCodes = new String[aircraftManagementDatabase.maxMRs];
		for (int i = 0; i < aircraftManagementDatabase.maxMRs; i++) {
			DBFlightCodes[i] = aircraftManagementDatabase.getFlightCode(i);
		}
		for (int i = 0; i < aircraftManagementDatabase.maxMRs; i++) {
			if (aircraftManagementDatabase.getFlightCode(i) != null
					&& !Arrays.asList(flightCodes).contains(aircraftManagementDatabase.getFlightCode(i))) {
				if(aircraftManagementDatabase.getStatus(i) == 8 ||aircraftManagementDatabase.getStatus(i) == 9 || aircraftManagementDatabase.getStatus(i) == 10 
						|| aircraftManagementDatabase.getStatus(i) == 11 || aircraftManagementDatabase.getStatus(i) == 12)
				 {
					listModel.addElement(aircraftManagementDatabase.getFlightCode(i));
					size++;
					break;
				 }
			}
		}
		for (int i = 0; i < size; i++) {
			if (!Arrays.asList(DBFlightCodes).contains(listModel.elementAt(i))) 
			{
					listModel.remove(i);
					size--;
					break;
			}
		}
		for(int i = 0; i < size; i++)
		{
			for(int j = 0; j < aircraftManagementDatabase.maxMRs; j++)
			{
				if(listModel.elementAt(i).equals(aircraftManagementDatabase.getFlightCode(j)))
				{
					if(aircraftManagementDatabase.getStatus(j) != 8 && aircraftManagementDatabase.getStatus(j) != 9 && aircraftManagementDatabase.getStatus(j) != 10
							&& aircraftManagementDatabase.getStatus(j) != 11 && aircraftManagementDatabase.getStatus(j) != 12)
					{
						listModel.remove(i);
						size--;
						break;
					}
				}
			}
		}
	} // update

	/*
	 * Checks for selected aircrafts from the list as well as performs status
	 * changes depending on buttons selected
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		selectedIndex = list.getSelectedIndex();
		int mrIndex = -1;
		for(int i = 0; i < aircraftManagementDatabase.maxMRs; i++)
		 { 
			 String fc = aircraftManagementDatabase.getFlightCode(i);
			 if(listModel.get(selectedIndex).toString().equals(fc))
			 {
				 mrIndex = i;
				 break;
			 }
		 }
		if (e.getSource() == FaultFound) {
			String fault_description = JOptionPane.showInputDialog("Enter fault");

			if (fault_description == null) {  // cancel was pressed
                return;
            }

            if (!fault_description.trim().isEmpty()) { // something written - okay pressed
                aircraftManagementDatabase.faultsFound(mrIndex, fault_description);
                info.append(aircraftManagementDatabase.getFlightCode(mrIndex) + " Fault(s) found: " + fault_description + "\n");
            } else { //  nothing written - okay pressed
            	JOptionPane.showMessageDialog(null, "Please enter a fault");
            }
		}

		if (e.getSource() == FaultyAwaitingCleaning) {
			aircraftManagementDatabase.setStatus(mrIndex, 12);
			info.append(aircraftManagementDatabase.getFlightCode(mrIndex) + " Fault(s) found, ready to clean\n");
		}

		if (e.getSource() == OKAwaitCleaning) {
			aircraftManagementDatabase.setStatus(mrIndex, 11);
			info.append(aircraftManagementDatabase.getFlightCode(mrIndex) + " Maintanance succesfull, ready to clean\n");
		}

		if (e.getSource() == OKCheck) {
			aircraftManagementDatabase.setStatus(mrIndex, 13);
			info.append(aircraftManagementDatabase.getFlightCode(mrIndex) + " Has been checked and cleaned,\nready to refuel\n");
		}
		
		if (e.getSource() == Repair) {
			aircraftManagementDatabase.setStatus(mrIndex, 8);
			info.append(aircraftManagementDatabase.getFlightCode(mrIndex) + " Has been repaired, ready for checks\n");
		}

		list.setSelectedIndex(selectedIndex);
		list.ensureIndexIsVisible(selectedIndex);
	}

	private void showTextPrompt(String string) {
		
	}//not used

	/* Enables/disables buttons based on the status of the aircraft */
	@Override
	public void valueChanged(ListSelectionEvent e) 
	{
		 checkValue();
	}
	
	public void checkValue()
	{
		String fc = null;
		 if(list.getSelectedIndex() != -1)
			 fc = listModel.elementAt(list.getSelectedIndex());
		 int which = -1;
		 if(fc != null)
		 {
			 for(int i = 0; i < aircraftManagementDatabase.maxMRs; i++)
			 {
				 if(fc.equals(aircraftManagementDatabase.getFlightCode(i)))
				 {
					 which = i;
					 setLabels(which);
					 break;
				 }
			 }
		 }
		 //All buttons disabled by default
		 if(size == 0 || list.getSelectedIndex() == -1)
		 {
			 FaultFound.setEnabled(false);
			 FaultyAwaitingCleaning.setEnabled(false);
			 OKAwaitCleaning.setEnabled(false);
			 OKCheck.setEnabled(false);
			 setLabels(-1);
		 }
		 else
		//Enable/disable buttons depending on status
		 {
			 if(which != -1 && (aircraftManagementDatabase.getStatus(which) == 10 || aircraftManagementDatabase.getStatus(which) == 8))
				 FaultFound.setEnabled(true);
			 else
				 FaultFound.setEnabled(false);
			 if(which != -1 && aircraftManagementDatabase.getStatus(which) == 9)
				 FaultyAwaitingCleaning.setEnabled(true);
			 else
				 FaultyAwaitingCleaning.setEnabled(false);
			 if(which != -1 && aircraftManagementDatabase.getStatus(which) == 8)
				 OKAwaitCleaning.setEnabled(true);
			 else
				 OKAwaitCleaning.setEnabled(false);
			 if(which != -1 && aircraftManagementDatabase.getStatus(which) == 10)
				 OKCheck.setEnabled(true);
			 else
				 OKCheck.setEnabled(false);
			 if(which != -1 && aircraftManagementDatabase.getStatus(which) == 12)
				 Repair.setEnabled(true);
			 else
				 Repair.setEnabled(false);
		 }
	}

}
