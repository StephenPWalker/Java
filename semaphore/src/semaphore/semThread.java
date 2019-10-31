package semaphore;
import java.util.concurrent.Semaphore;

public class semThread extends Thread
{
	String name;
	Semaphore sem;
	public semThread(String name, Semaphore sem)
	{
		this.name = name;
		this.sem = sem;
	}
	public void run()
	{
		System.out.println(name + " aquiring lock!");
		System.out.println(name + " availiable permits: " + sem.availablePermits());
		try 
		{
			sem.acquire();
			System.out.println(name + " got permit!");
			try
			{
				for(int i = 0; i < 10; i++)
				{
					System.out.println(name + " Performing operation: " + (i + 1) + " availiable permits: " + sem.availablePermits());
					Thread.sleep(500);
				}
			}
			finally
			{
				System.out.println(name + " releasing lock");
				sem.release();
				System.out.println(name + " availiable permits: " + sem.availablePermits());
			}
		}
		catch (InterruptedException e) 
		{
			
			e.printStackTrace();
		}		
	}
}
