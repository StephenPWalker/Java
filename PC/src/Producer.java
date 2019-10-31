public class Producer extends Thread
{
	Queue q;
	String name;
	public Producer(Queue q, String n)
	{
		this.q = q;
		name = n;
	}
	public void run()
	{
		while(true)
		{
			try 
			{
				q.Produce(name);
				Thread.sleep(500);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}	
		}
	}
}
