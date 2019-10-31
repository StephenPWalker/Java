
public class Producer extends Thread
{
	Queue q;
	public Producer(Queue q)
	{
		this.q = q;
	}
	public void run()
	{
		while(true)
		{
			try 
			{
				q.produce();
				Thread.sleep(300);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}			
		}
	}
}
