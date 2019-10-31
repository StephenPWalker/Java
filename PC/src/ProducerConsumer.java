public class ProducerConsumer
{	
	public static void main(String[] args) 
	{
		int amount = 3;
		Queue q = new Queue();
		Producer[] producer = new Producer[amount + 3];
		Consumer[] consumer = new Consumer[amount + 3];
		for(int i = 0; i < amount; i++)
		{
			producer[i] = new Producer(q , "" + i);
	    	producer[i].start();
	    	
	    	consumer[i] = new Consumer(q, "" + i);	
	    	consumer[i].start();
		}   
		producer[amount + 1] = new Producer(q, "" + (amount + 1));
		producer[amount + 1].start();
		producer[amount + 2] = new Producer(q, "" + (amount + 2));
		producer[amount + 2].start();
	}
}
