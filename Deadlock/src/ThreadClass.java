
public class ThreadClass extends Thread
{
	Object lock1;
	Object lock2;
	int identity;
	String l1 = "";
	String l2 = "";
	public ThreadClass(Object lock1, Object lock2, Object lock3, Object lock4, int identity)
	{
		if(identity == 1)
		{
			this.lock1 = lock1;
			this.lock2 = lock3;
			l1 = "1";
			l2 = "3";
		}
		else if(identity == 2)
		{
			this.lock1 = lock3;
			this.lock2 = lock2;
			l1 = "3";
			l2 = "2";
		}
		else if(identity == 3)
		{
			this.lock1 = lock4;
			this.lock2 = lock1;
			l1 = "4";
			l2 = "1";
		}
		else if(identity == 4)
		{
			this.lock1 = lock2;
			this.lock2 = lock4;
			l1 = "2";
			l2 = "4";
			//for deadlock release change order of these locks
		}
		this.identity = identity;
	}
	
	public void run()
	{
		while(true)
		{
			firstLock();
			try
			{
				Thread.sleep(100);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	public void firstLock()
	{
		synchronized(lock1)
		{
			System.out.println("Thread" + identity + ": Holding lock " + l1 + "...");
			try 
			{
				Thread.sleep(400);
			} 
			catch (InterruptedException e1) 
			{
				e1.printStackTrace();
			}
			System.out.println("Thread" + identity + ": Waiting for lock " + l2 + "...");
			secondLock();
		}	
	}
	public void secondLock()
	{
		synchronized(lock2)
		{
			System.out.println("Thread" + identity + ": Holding lock " + l1 + " & " + l2 + "...");
			for(int i = 0; i < 10; i++)
			{
				System.out.println("Thread" + identity + ": Doing operation " + (i + 1));
				try
				{
					Thread.sleep(100);
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
