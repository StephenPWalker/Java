import java.util.Vector;

public class Queue 
{
	private Vector<Integer> list = new Vector<Integer>();
	private int value;
	private int capacity = 100;
	
	public synchronized void Consume(String name) throws InterruptedException
	{
		while(list.size() == 0)
			wait();
		list.remove(value - 1);
		System.out.println("Consumer " + name + " consumed : " + value);	
		value--;
		notify();
	}
	
	public synchronized void Produce(String name) throws InterruptedException
	{
		while(list.size() == capacity)
			wait();
		
		list.add(1);
		value++;
		System.out.println("Producer " + name +" produced : " + value);	
		notify();			
	}
}
