public class Consumer extends Thread
{
	Queue q;
	String name;
	public Consumer(Queue q, String n)
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
				q.Consume(name);
				Thread.sleep(300);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}			
		}
	}
}
