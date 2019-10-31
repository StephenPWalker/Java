public class DiningMain 
{	

	public static void main(String[] args) 
	{		
		Person[] person = new Person[5]; 
		Object[] fork = new Object[5];
		
		for(int i = 0; i < fork.length; i ++)
			fork[i] = new Object();
		
		for(int i = 0; i < person.length; i++)
		{
			if(i < person.length - 1)
			{
				person[i] = new Person(fork[i + 1], fork[i], i + 1, i + 2 , i + 1);
				person[i].start();
			}
			else
			{
				person[i] = new Person(fork[0], fork[i], i + 1, 1, i + 1);
				person[i].start();
			}
		}
	}
}
