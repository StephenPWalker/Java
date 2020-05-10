import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * An interface to SAAMS: Refuelling Supervisor Screen: Inputs events from the
 * Refuelling Supervisor, and displays aircraft information. This class is a
 * controller for the AircraftManagementDatabase: sending it messages to change
 * the aircraft status information. This class also registers as an observer of
 * the AircraftManagementDatabase, and is notified whenever any change occurs in
 * that <<model>> element. See written documentation.
 * 
 * @stereotype boundary/view/controller
 * @url element://model:project::SAAMS/design:view:::id15rnfcko4qme4cko4swib
 * @url element://model:project::SAAMS/design:node:::id15rnfcko4qme4cko4swib.node107
 * @url element://model:project::SAAMS/design:view:::id3y5z3cko4qme4cko4sw81
 */
public class RefuellingSupervisor extends JFrame implements ActionListener, Observer, ListSelectionListener {
	/**
	 * The Refuelling Supervisor Screen interface has access to the
	 * AircraftManagementDatabase.
	 * 
	 * @supplierCardinality 1
	 * @clientCardinality 1
	 * @label accesses/observes
	 * @directed
	 */

	// Initialise variables
	private AircraftManagementDatabase aircraftManagementDatabase;
	private int selectedIndex;
	int size = 0;

	private JLabel FlightCode;
	private JLabel FlightStatus;
	private JButton ReadyRefuel;

	private JTextArea info; // As it says
	private JList<String> list; // As List of aircraft
	private DefaultListModel<String> listModel;

	public RefuellingSupervisor(AircraftManagementDatabase aircraftManagementDatabase) {

		this.aircraftManagementDatabase = aircraftManagementDatabase;

		// setup Java Swing frame
		setTitle("Refuelling Supervisor Conroller");
		setLocation(430, 400);
		setSize(400, 320);
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

		// button for completing refuel
		ReadyRefuel = new JButton("Refuel");
		window.add(ReadyRefuel);
		ReadyRefuel.addActionListener(this);

		// label for code of the flight
		FlightCode = new JLabel("[FLIGHT CODE]", SwingConstants.CENTER);
		window.add(FlightCode);

		// label for status of the flight
		FlightStatus = new JLabel("[FLIGHT STATUS]", SwingConstants.CENTER);
		window.add(FlightStatus);
		ReadyRefuel.setEnabled(false);

		// Display the frame
		setVisible(true);
		setResizable(false);

		// Subscribe to the model
		aircraftManagementDatabase.addObserver(this);
	}

	/*
	 * Changes the labels based on the aircraft selected from the list in order to
	 * provide basic information
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
	 * Receives and sends updates to and from the database of the aircrafts which
	 * enter the status for refuelling, and adds them to the list
	 */
	@Override
	public void update(Observable arg0, Object arg1) {

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
				if (aircraftManagementDatabase.getStatus(i) == 13) {
					listModel.addElement(aircraftManagementDatabase.getFlightCode(i));
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
					if (aircraftManagementDatabase.getStatus(j) != 13) {
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

		int mrIndex = -1;
		selectedIndex = list.getSelectedIndex();
		for (int i = 0; i < aircraftManagementDatabase.maxMRs; i++) {
			String fc = aircraftManagementDatabase.getFlightCode(i);
			if (listModel.get(selectedIndex).toString().equals(fc)) {
				mrIndex = i;
				break;
			}
		}
		if (e.getSource() == ReadyRefuel) {
			aircraftManagementDatabase.setStatus(mrIndex, 14);
			info.append(aircraftManagementDatabase.getFlightCode(mrIndex) + " Fuelling completed \n");
		}

		list.setSelectedIndex(selectedIndex);
		list.ensureIndexIsVisible(selectedIndex);

	}

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

		// All buttons disabled by default
		if (size == 0 || list.getSelectedIndex() == -1) {
			ReadyRefuel.setEnabled(false);
			setLabels(-1);
		}
		// Enable/disable buttons depending on status
		else 
		{
			if (which != -1 && aircraftManagementDatabase.getStatus(which) == 13)
				ReadyRefuel.setEnabled(true);
			else
				ReadyRefuel.setEnabled(false);
		}
	}
}
