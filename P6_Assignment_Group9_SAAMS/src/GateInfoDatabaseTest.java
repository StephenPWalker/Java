import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class GateInfoDatabaseTest {

	private final int MCode1 = 111;
	private final int MCode2 = 222;
	private final int MCode3 = 333;
	private final int gate1 = 0;
	private final int gate2 = 1;
	private final int gate3 = 2;
	private GateInfoDatabase db;

	/**
	 * Checks initial statuses of gates 
	 * This is to ensure all the three
	 * gates that are initialised do start
	 * with status of 'Free' and not other
	 * statuses (NOT RESERVED or OCCUPIED)
	 */
	@Test
    public void checkItitialStatuses() {
		
        for (int i = 0; i < GateInfoDatabase.maxGateNumber; i++) {
            assertEquals("Initial status is not FREE at index " + i, 0, db.getStatus(i));
        }
    }
	
	/**
	 * Initialises a new GateInfoDatabase for testing.
	 * This allows us to use the GateInfoDatabase
	 * in order to conduct our JUnit testing
	 *  
	 */
	@Before
	public void Before() {
		db = new GateInfoDatabase();
	}
	
	/**
     * Checks for status change in gate from FREE to RESERVED
     * This is to ensure that gates can change status from 'Free'
     * To 'Reserved'.
     */
    @Test
    public void checkAllocate() {
    	
        db.allocate(gate1, MCode1);
        assertEquals("Gate status is not RESERVED at gate 1", 1, db.getStatus(gate1));
        db.allocate(gate2, MCode1);
        assertEquals("Gate status is not RESERVED at gate 2", 1, db.getStatus(gate2));
        db.allocate(gate3, MCode1);
        assertEquals("Gate status is not RESERVED at gate 3", 1, db.getStatus(gate3));
        db.allocate(-1, MCode1);
        assertEquals("Gate status is not RESERVED at gate -1", -1, db.getStatus(-1));
        db.allocate(4, MCode1);
        assertEquals("Gate status is not RESERVED at gate 4", -1, db.getStatus(4));
    }
    
    /**
     * Checks for status change in gate from RESERVED to OCCUPIED
     * This is to ensure that gates can change status from 'Reserved'
     * to 'Occupied'.
     */
    @Test
    public void checkDocked() {
    	
        db.allocate(gate1, MCode2);
        db.docked(gate1);
        assertEquals("Gate status is not OCCUPIED at gate 1", 2, db.getStatus(gate1));
        db.allocate(gate2, MCode2);
        db.docked(gate2);
        assertEquals("Gate status is not OCCUPIED at gate 2", 2, db.getStatus(gate2));
        db.allocate(gate3, MCode2);
        db.docked(gate3);
        assertEquals("Gate status is not OCCUPIED at gate 3", 2, db.getStatus(gate3));
        db.allocate(-1, MCode2);
        db.docked(-1);
        assertEquals("Gate status is not at gate -1", -1, db.getStatus(-1));
        db.allocate(4, MCode2);
        db.docked(4);
        assertEquals("Gate status is not at gate 4", -1, db.getStatus(4));
    }
    
    /**
     * Checks if the status of the gate is FREE again after departure.
     * This is to ensure that gates can change status from 'Occupied'
     * to 'Free'.
     */
    @Test
    public void checkDeparted() {
    	
        db.allocate(gate3, MCode3);
        db.docked(gate3);
        db.departed(gate3);
        assertEquals("Gate status should be FREE", 0, db.getStatus(gate3));
        db.allocate(gate2, MCode3);
        db.docked(gate2);
        db.departed(gate2);
        assertEquals("Gate status should be FREE", 0, db.getStatus(gate2));
        db.allocate(gate1, MCode3);
        db.docked(gate1);
        db.departed(gate1);
        assertEquals("Gate status should be FREE", 0, db.getStatus(gate1));
        db.allocate(-1, MCode3);
        db.docked(-1);
        db.departed(-1);
        assertEquals("Gate status should be FREE", -1, db.getStatus(-1));
        db.allocate(4, MCode3);
        db.docked(4);
        db.departed(4);
        assertEquals("Gate status should be FREE", -1, db.getStatus(4));
    }
    
    /**
     * Checks if the status changes to docked when trying to dock to a FREE gate without being reserved.
     */
    @Test
    public void docked2() {
    
    	db.docked(gate2);
        assertEquals("Docked to a FREE gate", 0, db.getStatus(gate2));
        db.docked(gate3);
        assertEquals("Docked to a FREE gate", 0, db.getStatus(gate3));
        db.docked(gate1);
        assertEquals("Docked to a FREE gate", 0, db.getStatus(gate1));
        db.docked(-1);
        assertEquals("Docked to a FREE gate", -1, db.getStatus(-1));
        db.docked(4);
        assertEquals("Docked to a FREE gate", -1, db.getStatus(4));
    }
    
    
}
