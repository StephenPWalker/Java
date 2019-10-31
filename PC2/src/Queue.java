import java.util.Vector;
import java.util.Random;

public class Queue 
{
	private Vector<String> list = new Vector<String>();
	private int value = 0;
	private int capacity = 20;
	private String[] food = {"Beans","Carrots","Potatoes"};

	public synchronized void consume() throws InterruptedException
	{
		while(list.size() == 0)
			wait();
		
		list.remove();
		System.out.println("Consumer " + 1 + " consumed : " + list.indexOf(value - 1));	
		value--;
		notify();
	}
	public synchronized void produce() throws InterruptedException
	{
		Random rand = new Random();
		int n = rand.nextInt(3);
		String which = food[n];
		
		while(list.size() == capacity)
			wait();
		
		list.add(which);
		System.out.println("Producer " + 1 + " produced : " + food[n]);	
		value++;
		notify();
	}
}
