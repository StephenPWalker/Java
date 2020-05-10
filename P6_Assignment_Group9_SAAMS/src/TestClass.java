import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Convenient place to put the tests
 * Use one method for one class test of multiple methods for one class, either or.
 * WIll be useful if all in one place when we need to print out.
 * @author Stephen, Mate, Usman, Hristo
 */
public class TestClass 
{
	@Test
	public void managementRecordTests()
	{	
		/*
		 * CHECKS FOR EACH METHOD OF MANAGEMENT RECORD FROM TOP TO BOTTOM 
		 */
		for(int i = 0; i < 19; i++)
		{
			ManagementRecord MR = new ManagementRecord();
			MR.setStatus(i);
			if(i != 6)
				assertEquals("Status " + i + " has failed",i, MR.getStatus());
			else
				assertEquals("Status " + i + " has failed", 0, MR.getStatus());
		}
		ManagementRecord MR = new ManagementRecord();
		Itinerary I = new Itinerary("Edinburgh","Stirling","Stirling");
		Itinerary I2 = new Itinerary("Edinburgh","Paris","London");
		FlightDescriptor FD = new FlightDescriptor("A0123", I, null);
		FlightDescriptor FD2 = new FlightDescriptor("A4567", I2, null);
		//CHECK STATUS CHANGES
		MR.setStatus(0);
		MR.radarDetect(FD);
		assertEquals( "Status has failed - Waiting to Land", 2, MR.getStatus());
		MR.setStatus(0);
		MR.radarDetect(FD2);
		assertEquals("Status has failed - In transit", 1, MR.getStatus());
		MR.setStatus(1);
		MR.radarDetect(FD2);
		assertEquals("Status has failed - In transit", 1, MR.getStatus());
		MR.setStatus(1);
		MR.radarDetect(FD);
		assertEquals("Status has failed - In transit", 2, MR.getStatus());		
		//CHECK ALL ERROR HANDLING IS WORKING
		for(int i = 0; i < 19; i++)
		{
			MR.setStatus(i);
			MR.radarLostContact();
			if(i == 1 || i == 18)
				assertEquals("Status not reset",0, MR.getStatus());
			else
				if(i != 6)
				assertEquals("Status change failure", i, MR.getStatus());
		}
	}
	@Test
	public void aircraftManagementDatabaseTest()
	{
		/*
		 * CHECKS FOR EACH METHOD OF AIRCRAFT MANAGEMENT DATABASE FROM TOP TO BOTTOM 
		 */
		AircraftManagementDatabase AMD = new AircraftManagementDatabase();
		//SET STATUS GET STATUS
		for(int i = 0; i < AMD.maxMRs; i++)
		{
			assertEquals("Status not set to FREE", 0, AMD.getStatus(i));
		}
		AMD.setStatus(0, 6);
		assertEquals("Status not set to FREE", 0, AMD.getStatus(0));
		AMD.setStatus(0, 20);
		assertEquals("Status not set to FREE", 0, AMD.getStatus(0));
		AMD.setStatus(0, -1);
		assertEquals("Status not set to FREE", 0, AMD.getStatus(0));

		for(int i = 0; i < 19; i++)
		{
			//IGNORE AS HANDLED
			if(i != 6)
			{
				AMD.setStatus(0, i);
				assertEquals("Status not set to " + i, i, AMD.getStatus(0));
			}
		}
		//CHECK FLIGHT CODE IS NULL
		assertEquals("FlightCode is not null", null, AMD.getFlightCode(0));
		AMD.setStatus(3, 3);
		AMD.setStatus(7, 3);
		AMD.setStatus(9, 3);
		AMD.setStatus(1, 16);
		AMD.setStatus(4, 16);
		AMD.setStatus(8, 16);
		//GET MCODE FROM STATUS
		int[] arr = AMD.getWithStatus(3);
		for(int i = 0; i < arr.length; i ++)
		{
			if(i == 0)
				assertEquals("Index not correct", 3, arr[i]);
			if(i == 1)
				assertEquals("Index not correct", 7, arr[i]);
			if(i == 2)
				assertEquals("Index not correct", 9, arr[i]);
		}
		arr = AMD.getWithStatus(16);
		for(int i = 0; i < arr.length; i ++)
		{
			if(i == 0)
				assertEquals("Index not correct", 1, arr[i]);
			if(i == 1)
				assertEquals("Index not correct", 4, arr[i]);
			if(i == 2)
				assertEquals("Index not correct", 8, arr[i]);
		}
		//EDGE CASES
		arr =  AMD.getWithStatus(-1);
		assertEquals("Array not null", null, arr);
		arr =  AMD.getWithStatus(19);
		assertEquals("Array not null", null, arr);
		
		// CANT CHECK TAXITO OR FAULTSFOUND DUE TO ERROR HANDLING
		
		for(int i = 0; i < AMD.maxMRs; i ++)
			AMD.setStatus(i, 0);
		for(int i = 0; i < AMD.maxMRs; i ++)
		{
			AMD.setStatus(i, i);
			if(i != 6)
				assertEquals("Status change failed", i, AMD.getStatus(i));
			else
				assertEquals("Error handling failed", 0,AMD.getStatus(i));
		}
		// CHECK RADAR LOST
		for(int i = 0; i < AMD.maxMRs; i++)
		{
			AMD.radarLostContact(i);
			if(i == 1)
				assertEquals("Radar lost contact failed", 0, AMD.getStatus(i));
			else if(i != 6)
				assertEquals("Error handling failed", i, AMD.getStatus(i));
		}
	}
}
