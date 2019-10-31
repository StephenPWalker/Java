
public class Consumer extends Thread
{
	Queue q;
	public Consumer(Queue q)
	{
		this.q = q;
	}
	public void run()
	{
		while(true)
		{
			try 
			{
				q.consume();
				Thread.sleep(300);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}			
		}
	}
}
