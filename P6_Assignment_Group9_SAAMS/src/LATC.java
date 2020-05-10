
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * An interface to SAAMS: Local Air Traffic Controller Screen: Inputs events
 * from LATC (a person), and displays aircraft information. This class is a
 * controller for the AircraftManagementDatabase: sending it messages to change
 * the aircraft status information. This class also registers as an observer of
 * the AircraftManagementDatabase, and is notified whenever any change occurs in
 * that <<model>> element. See written documentation.
 * 
 * @stereotype boundary/view/controller
 * @url element://model:project::SAAMS/design:view:::id15rnfcko4qme4cko4swib
 * @url element://model:project::SAAMS/design:view:::id2fh3ncko4qme4cko4swe5
 * @url element://model:project::SAAMS/design:node:::id15rnfcko4qme4cko4swib.node107
 * @url element://model:project::SAAMS/design:view:::idwwyucko4qme4cko4sgxi
 */
public class LATC extends JFrame implements ActionListener, Observer, ListSelectionListener {
	/**
	 * The Local Air Traffic Controller Screen interface has access to the
	 * AircraftManagementDatabase.
	 * 
	 * @supplierCardinality 1
	 * @clientCardinality 1
	 * @label accesses/observes
	 * @directed
	 */

	//Initialise variables
	private AircraftManagementDatabase aircraftManagementDatabase;
	private int selectedIndex;

	private JLabel FlightCode;
	private JLabel FlightStatus;
	private JButton GrantLandingPermission;
	private JButton ConfirmLanding;
	private JButton AwaitTaxiingPermission;
	private JButton GrantTakeOffPermission;
	private JButton GetInfo;
	private JTextArea info; // As it says
	private JList<String> list; // As List of aircraft
	private DefaultListModel<String> listModel;
	private int size = 0;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public LATC(AircraftManagementDatabase aircraftManagementDatabase) {

		this.aircraftManagementDatabase = aircraftManagementDatabase;

		//setup Java Swing frame
		setTitle("LATC Controller");
		setLocation(1310, 40);
		setSize(500, 340);
		Container window = getContentPane();
		window.setLayout(new FlowLayout());
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
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

		//button for granting landing permission
		GrantLandingPermission = new JButton("Grant Landing Permission");
		window.add(GrantLandingPermission);
		GrantLandingPermission.addActionListener(this);
		GrantLandingPermission.setEnabled(false); //disabled by default


		//button for dock plane
		ConfirmLanding = new JButton("Confirm Landing");
		window.add(ConfirmLanding);
		ConfirmLanding.addActionListener(this);
		ConfirmLanding.setEnabled(false); //disabled by default

		//button to notify of an aircraft awaiting taxiing permission
		AwaitTaxiingPermission = new JButton("Await Taxiing Permission");
		window.add(AwaitTaxiingPermission);
		AwaitTaxiingPermission.addActionListener(this);
		AwaitTaxiingPermission.setEnabled(false); //disabled by default


		//button for granting take-off permission
		GrantTakeOffPermission = new JButton("Grant TakeOff Permission");
		window.add(GrantTakeOffPermission);
		GrantTakeOffPermission.addActionListener(this);
		GrantTakeOffPermission.setEnabled(false); //disabled by default

		//button to receive details of the flight
		GetInfo = new JButton("Flight Info");
		window.add(GetInfo);
		GetInfo.addActionListener(this);
		GetInfo.setEnabled(false); //disabled by default

		//label for flight code
		FlightCode = new JLabel("[FLIGHT CODE]", SwingConstants.CENTER);
		window.add(FlightCode);
		
		//label for flight status
		FlightStatus = new JLabel("[FLIGHT STATUS]", SwingConstants.CENTER);
		window.add(FlightStatus);

		// Display the frame
		setVisible(true);
		setResizable(false);
		
		// Subscribe to the model
		aircraftManagementDatabase.addObserver(this);
	}

	/*
	 * Checks for selected aircrafts from the list as well as performs status
	 * changes depending on buttons selected
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		selectedIndex = list.getSelectedIndex();
		int mrIndex = -1;
		for (int i = 0; i < aircraftManagementDatabase.maxMRs; i++) {
			String fc = aircraftManagementDatabase.getFlightCode(i);
			if (listModel.get(selectedIndex).toString().equals(fc)) {
				mrIndex = i;
				break;
			}
		}
		if (e.getSource() == GrantLandingPermission) {
			aircraftManagementDatabase.setStatus(mrIndex, 4);
			info.append(aircraftManagementDatabase.getFlightCode(mrIndex) + " Landing permission granted.\n");
		}

		if (e.getSource() == ConfirmLanding) {
			aircraftManagementDatabase.setStatus(mrIndex, 5);
			info.append(aircraftManagementDatabase.getFlightCode(mrIndex)
					+ " Landing confirmed, use GOC to assign gate.\n");
		}

		if (e.getSource() == AwaitTaxiingPermission) {
			aircraftManagementDatabase.setStatus(mrIndex, 16);
			info.append(aircraftManagementDatabase.getFlightCode(mrIndex) + " Awaiting taxiing permission. Permit in GOC.\n");
		}

		if (e.getSource() == GrantTakeOffPermission) {
			aircraftManagementDatabase.setStatus(mrIndex, 18);
			info.append(aircraftManagementDatabase.getFlightCode(mrIndex) + " Take off permission granted.\n");
		}

		if (e.getSource() == GetInfo) {

			int passengerNum = aircraftManagementDatabase.getPassengerList(mrIndex).getSize();

			JOptionPane.showMessageDialog(null,
					"Flight Code: " + aircraftManagementDatabase.getFlightCode(mrIndex) + "\nFlight Status: "
							+ aircraftManagementDatabase.getStatusName(mrIndex) + "\nFrom: "
							+ aircraftManagementDatabase.getItinerary(mrIndex).getFrom() + "\nTo: "
							+ aircraftManagementDatabase.getItinerary(mrIndex).getTo() + "\nNext Destination: "
							+ aircraftManagementDatabase.getItinerary(mrIndex).getNext()
							+ "\nNumber of Passengers on board: " + passengerNum);
		}
		list.setSelectedIndex(selectedIndex);
		list.ensureIndexIsVisible(selectedIndex);
	}

	/*
	 * Changes the labels based on the aircraft selected from the list in order to
	 *  provide basic information
	 */
	private void setLabels(int valueSelected) {
		if (valueSelected != -1) {
			FlightCode.setText(aircraftManagementDatabase.getFlightCode(valueSelected));
			FlightStatus.setText(aircraftManagementDatabase.getStatusName(valueSelected));
		} else {
			FlightCode.setText("[FLIGHT CODE]");
			FlightStatus.setText("[FLIGHT STATUS]");
		}
	}

	/* 
	 * Receives and sends updates to and from the database regards
	 *  the aircrafts which enter the statuses given, and adds them to the list
	 *  */
	@Override
	public void update(Observable o, Object arg) {
		if (list.getSelectedIndex() != -1)
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
				if (aircraftManagementDatabase.getStatus(i) == 0 || aircraftManagementDatabase.getStatus(i) == 1
						|| aircraftManagementDatabase.getStatus(i) == 2 || aircraftManagementDatabase.getStatus(i) == 3
						|| aircraftManagementDatabase.getStatus(i) == 4 || aircraftManagementDatabase.getStatus(i) == 15
						|| aircraftManagementDatabase.getStatus(i) == 16
						|| aircraftManagementDatabase.getStatus(i) == 17
						|| aircraftManagementDatabase.getStatus(i) == 18) {
					listModel.addElement(aircraftManagementDatabase.getFlightCode(i));
					info.append("Status of flight " + aircraftManagementDatabase.getFlightCode(i) + " : "
							+ aircraftManagementDatabase.getStatusName(i) + "\n");
					size++;
					break;
				}
			}
		}
		for (int i = 0; i < size; i++) {
			if (!Arrays.asList(DBFlightCodes).contains(listModel.elementAt(i))) {
				listModel.remove(i);
				size--;
				break;
			}
		}
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < aircraftManagementDatabase.maxMRs; j++) {
				if (listModel.elementAt(i).equals(aircraftManagementDatabase.getFlightCode(j))) {
					if (aircraftManagementDatabase.getStatus(j) != 0 && aircraftManagementDatabase.getStatus(j) != 1
							&& aircraftManagementDatabase.getStatus(j) != 2
							&& aircraftManagementDatabase.getStatus(j) != 3
							&& aircraftManagementDatabase.getStatus(j) != 4
							&& aircraftManagementDatabase.getStatus(j) != 15
							&& aircraftManagementDatabase.getStatus(j) != 16
							&& aircraftManagementDatabase.getStatus(j) != 17
							&& aircraftManagementDatabase.getStatus(j) != 18) {
						listModel.remove(i);
						size--;
						break;
					}
				}
			}
		}
	} // update

	/* Enables/disables buttons based on the status of the aircraft */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		checkValue();
	}

	public void checkValue() {
		String fc = null;
		if (list.getSelectedIndex() != -1)
			fc = listModel.elementAt(list.getSelectedIndex());
		int which = -1;
		if (fc != null) {
			for (int i = 0; i < aircraftManagementDatabase.maxMRs; i++) {
				if (fc.equals(aircraftManagementDatabase.getFlightCode(i))) {
					which = i;
					setLabels(which);
					break;
				}
			}
		}
		//All buttons disabled by default
		if (size == 0 || list.getSelectedIndex() == -1) {
			GrantLandingPermission.setEnabled(false);
			ConfirmLanding.setEnabled(false);
			AwaitTaxiingPermission.setEnabled(false);
			GrantTakeOffPermission.setEnabled(false);
			GetInfo.setEnabled(false);
			setLabels(-1);
		} 
		//Enable/disable buttons depending on status
		else {
			

			GetInfo.setEnabled(true);
			if (which != -1 && aircraftManagementDatabase.getStatus(which) == 3)
				GrantLandingPermission.setEnabled(true);
			else
				GrantLandingPermission.setEnabled(false);
			if (which != -1 && aircraftManagementDatabase.getStatus(which) == 4)
				ConfirmLanding.setEnabled(true);
			else
				ConfirmLanding.setEnabled(false);
			if (which != -1 && aircraftManagementDatabase.getStatus(which) == 15)
				AwaitTaxiingPermission.setEnabled(true);
			else
				AwaitTaxiingPermission.setEnabled(false);
			if (which != -1 && aircraftManagementDatabase.getStatus(which) == 17)
				GrantTakeOffPermission.setEnabled(true);
			else
				GrantTakeOffPermission.setEnabled(false);

		}
	}

}
