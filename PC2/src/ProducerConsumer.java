
public class ProducerConsumer 
{

	public static void main(String[] args) 
	{
		Queue q = new Queue();
		Producer producer = new Producer(q);
		producer.start();
		Consumer consumer = new Consumer(q);
		consumer.start();
	}

}
