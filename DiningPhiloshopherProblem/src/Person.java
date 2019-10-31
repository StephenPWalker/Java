
public class Person extends Thread
{
	private Object leftFork;
	private Object rightFork;
	private int pName;
	private int lFName;
	private int rFName;
	public Person(Object lFork, Object rFork, int me, int lf, int rf)
	{
		pName = me;
		if(pName == 3)
		{
			leftFork = rFork;
			rightFork = lFork;
			lFName = rf;
			rFName = lf;
		}
		else
		{
			leftFork = lFork;
			rightFork = rFork;
			lFName = lf;
			rFName = rf;
		}		
	}
	
	public void run()
	{
		while(true)
		{
			try 
			{
				pickUpLeftFork();
				System.out.println("Philosopher " + pName + " thinking about life");
				Thread.sleep(400);
			}
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}			
		}
	}
	public void pickUpRightFork()
	{
		synchronized(rightFork)
		{
			System.out.println("Philosopher " + pName + " picked up right fork " + rFName);
			try 
			{
				Thread.sleep(400);
			} 
			catch (InterruptedException e1) 
			{
				e1.printStackTrace();
			}
			System.out.println("Philosopher " + pName + " eating...");
		}
	}
	public void pickUpLeftFork()
	{
		synchronized(leftFork)
		{
			System.out.println("Philosopher " + pName + " picked up left fork " + lFName);
			try 
			{
				Thread.sleep(400);
			} 
			catch (InterruptedException e1) 
			{
				e1.printStackTrace();
			}
			System.out.println("Philosopher " + pName + " waiting for right fork " + rFName);
			pickUpRightFork();
		}
	}
}
