
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * An interface to SAAMS: A Ground Operations Controller Screen: Inputs events
 * from GOC (a person), and displays aircraft and gate information. This class
 * is a controller for the GateInfoDatabase and the AircraftManagementDatabase:
 * sending them messages to change the gate or aircraft status information. This
 * class also registers as an observer of the GateInfoDatabase and the
 * AircraftManagementDatabase, and is notified whenever any change occurs in
 * those <<model>> elements. See written documentation.
 * 
 * @stereotype boundary/view/controller
 * @url element://model:project::SAAMS/design:node:::id2wdkkcko4qme4cko4svm2.node36
 * @url element://model:project::SAAMS/design:view:::id2wdkkcko4qme4cko4svm2
 * @url element://model:project::SAAMS/design:view:::id1un8dcko4qme4cko4sw27
 * @url element://model:project::SAAMS/design:view:::id1bl79cko4qme4cko4sw5j
 * @url element://model:project::SAAMS/design:view:::id15rnfcko4qme4cko4swib
 * @url element://model:project::SAAMS/design:node:::id15rnfcko4qme4cko4swib.node107
 */

public class GOC extends JFrame implements ActionListener, Observer, ListSelectionListener {
	/**
	 * The Ground Operations Controller Screen interface has access to the
	 * GateInfoDatabase.
	 * 
	 * @clientCardinality 1
	 * @supplierCardinality 1
	 * @label accesses/observes
	 * @directed
	 */
	private GateInfoDatabase gateInfoDatabase;
	/**
	 * The Ground Operations Controller Screen interface has access to the
	 * AircraftManagementDatabase.
	 * 
	 * @clientCardinality 1
	 * @supplierCardinality 1
	 * @label accesses/observes
	 * @directed
	 */
	
	//Initialise variables
	private AircraftManagementDatabase aircraftManagementDatabase;
	private int MRSelectedIndex;
	private int gateSelectedIndex = 0;
	int size = 0;

	private JLabel FlightCode;
	private JLabel FlightStatus;

	private JLabel GateNumber;
	private JLabel GateStatus;

	private JButton GrantGroundClearance;
	private JButton AllocateGate;
	private JButton PermitTaxi;

	private JTextArea info; // As it says
	private JList <String>list; // As List of aircraft
	private JList <String>gateList; // As List of gates available
	private DefaultListModel <String>listModel;
	private DefaultListModel <String>gateListModel;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public GOC(AircraftManagementDatabase aircraftManagementDatabase, GateInfoDatabase gateInfoDatabase) {

		this.aircraftManagementDatabase = aircraftManagementDatabase;
		this.gateInfoDatabase = gateInfoDatabase;

		//setup Java Swing frame
		setTitle("GOC");
		setLocation(40, 40);
		setSize(500, 360);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		Container window = getContentPane();
		window.setLayout(new FlowLayout());

		info = new JTextArea();
		JScrollPane pane = new JScrollPane(info);
	    pane.setPreferredSize(new Dimension(350, 200));
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
		
		gateListModel = new DefaultListModel();

		gateList = new JList(gateListModel);
		gateList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		gateList.setSelectedIndex(1);
		gateList.addListSelectionListener(this);
		gateList.setVisibleRowCount(gateInfoDatabase.maxGateNumber);
		
		JScrollPane gateListScrollPane = new JScrollPane(gateList);
		gateListScrollPane.setPreferredSize(new Dimension(60, 80));
		add(gateListScrollPane, BorderLayout.WEST);
		gateListModel.addElement("Gate 1");
		gateListModel.addElement("Gate 2");
		gateListModel.addElement("Gate 3");
		setVisible(true);
		
		window.setLayout(new FlowLayout());
		
		//button for granting ground clearance
		GrantGroundClearance = new JButton("Grant Ground Clearance");
		window.add(GrantGroundClearance);
		GrantGroundClearance.addActionListener(this);
		
		//button for allocating gate
		AllocateGate = new JButton("Allocate Gate");
		window.add(AllocateGate);
		AllocateGate.addActionListener(this);
		
		//button for permitting taxi
		PermitTaxi = new JButton("Permit Taxi");
		window.add(PermitTaxi);
		PermitTaxi.addActionListener(this);

		//label for flight code
		FlightCode = new JLabel("[FLIGHT CODE]", SwingConstants.CENTER);
		window.add(FlightCode);

		//label for flight status
		FlightStatus = new JLabel("[FLIGHT STATUS]", SwingConstants.CENTER);
		window.add(FlightStatus);
		GateNumber = new JLabel("[GATE  NUMBER]", SwingConstants.CENTER);

		//label for gate number
		window.add(GateNumber);

		//label for gate status
		GateStatus = new JLabel("[GATE STATUS]", SwingConstants.CENTER);
		window.add(GateStatus);
		
		//set buttons to false for error handling 
		GrantGroundClearance.setEnabled(false);
		AllocateGate.setEnabled(false);
		PermitTaxi.setEnabled(false);
		
		// Display the frame
		setVisible(true);
		setResizable(false);
		
		// Subscribe to the model
		gateInfoDatabase.addObserver(this);
		aircraftManagementDatabase.addObserver(this);
	}

/*
	 * Checks for selected aircrafts from the list as well as performs status
	 * changes depending on buttons selected
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		MRSelectedIndex = list.getSelectedIndex();
		gateSelectedIndex = gateList.getSelectedIndex();
		int mrIndex = -1;
		for(int i = 0; i < aircraftManagementDatabase.maxMRs; i++)
		 { 
			 String fc = aircraftManagementDatabase.getFlightCode(i);
			 if(listModel.get(MRSelectedIndex).toString().equals(fc))
			 {
				 mrIndex = i;
				 break;
			 }
		 }

		if (e.getSource() == GrantGroundClearance) {
			aircraftManagementDatabase.setStatus(mrIndex, 3);
			info.append(aircraftManagementDatabase.getFlightCode(mrIndex) + " Ground clearance granted, authorize in LATC.\n");
		}

		if (e.getSource() == AllocateGate) {
			if (gateInfoDatabase.getStatus(gateSelectedIndex) == 0)
			{
			gateInfoDatabase.allocate(gateSelectedIndex, mrIndex);
			aircraftManagementDatabase.taxiTo(mrIndex, gateSelectedIndex);
			info.append(aircraftManagementDatabase.getFlightCode(mrIndex) + " has been allocated to gate "
					+ (gateSelectedIndex + 1) + "\n");
			}
			else 
			{
				JOptionPane.showMessageDialog(null, "Cannot allocate gate. Gate is reserved by another aircraft");
			}
		}

		if (e.getSource() == PermitTaxi) {

			aircraftManagementDatabase.setStatus(mrIndex, 17);
            gateInfoDatabase.departed(aircraftManagementDatabase.getGateNumber(mrIndex));
            info.append(aircraftManagementDatabase.getFlightCode(mrIndex) + " Taxiing permitted. Grant takeoff in LATC.\n");
		}

		list.setSelectedIndex(MRSelectedIndex);
		list.ensureIndexIsVisible(MRSelectedIndex);

	}
	
	/*
	 * Changes the labels based on the aircraft selected from the list in order to
	 *  provide basic information
	 */
	private void setFlightLabels(int valueSelected)
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
	 * Changes the gate labels based on the gate selected from the list in order to
	 *  provide status information
	 */
	private void setGateLabels(int valueSelected)
	{
		if (valueSelected != -1) {
			GateNumber.setText("[GATE  NUMBER]: " + String.valueOf(valueSelected+1));
			GateStatus.setText("[GATE STATUS]: " + gateInfoDatabase.getStatusName(valueSelected));
		}
		else
		{
		GateNumber.setText("[GATE  NUMBER]");
		GateNumber.setText("[GATE STATUS]");
		}
	}
	
	/* 
	 * Receives and sends updates to and from the database regards
	 *  the aircrafts which enter the statuses given for Gate Console, and adds them to the list
	 *  */
	@Override
	public void update(Observable o, Object arg) {
		if(list.getSelectedIndex() != -1)
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
					if(aircraftManagementDatabase.getStatus(i) == 2 || aircraftManagementDatabase.getStatus(i) == 5 || 
							aircraftManagementDatabase.getStatus(i) == 16)
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
	} // update

	/* Enables/disables buttons based on the status of the aircraft */
	@Override
	public void valueChanged(ListSelectionEvent e) 
	{
		 checkValue();
	}
	public void checkValue()
	{
		int gateSelected = gateList.getSelectedIndex();
		setGateLabels(gateSelected);
		
		String fc = null;
		 if(list.getSelectedIndex() != -1)
			 fc = listModel.elementAt(list.getSelectedIndex());
		 int flightSelected = -1;
		 if(fc != null)
		 {
			 for(int i = 0; i < aircraftManagementDatabase.maxMRs; i++)
			 {
				 if(fc.equals(aircraftManagementDatabase.getFlightCode(i)))
				 {
					 flightSelected = i;
					 setFlightLabels(flightSelected);
					 break;
				 }
			 }
		     
		 }
		 
		//All buttons disabled by default
		 if(size == 0 || list.getSelectedIndex() == -1)
		 {
			 GrantGroundClearance.setEnabled(false);
			 AllocateGate.setEnabled(false);
			 PermitTaxi.setEnabled(false);
			 setFlightLabels(-1);
		 }
		 
		//Enable/disable buttons depending on status
		 else
		 {
			 if(flightSelected != -1 && aircraftManagementDatabase.getStatus(flightSelected) == 2)
				 GrantGroundClearance.setEnabled(true);
			 else
				 GrantGroundClearance.setEnabled(false);
			 if(flightSelected != -1 && aircraftManagementDatabase.getStatus(flightSelected) == 5 && gateSelected != -1)
				 AllocateGate.setEnabled(true);
			 else
				 AllocateGate.setEnabled(false);
			 if(flightSelected != -1 && aircraftManagementDatabase.getStatus(flightSelected) == 16)
				 PermitTaxi.setEnabled(true);
			 else
				 PermitTaxi.setEnabled(false);
		 }
	}
}
