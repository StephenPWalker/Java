package semaphore;
import java.util.concurrent.Semaphore;

public class semaphoreTest 
{
	static Semaphore sem = new Semaphore(4);
	
	public static void main(String[] args)
	{
		System.out.println("Availiable permits: " + sem.availablePermits());
		
		semThread st = new semThread("A", sem);
		st.start();
		semThread st2 = new semThread("B", sem);
		st2.start();
		semThread st3 = new semThread("C", sem);
		st3.start();
		semThread st4 = new semThread("D", sem);
		st4.start();
		semThread st5 = new semThread("E", sem);
		st5.start();
		semThread st6 = new semThread("F", sem);
		st6.start();
		semThread st7 = new semThread("G", sem);
		st7.start();
		semThread st8 = new semThread("H", sem);
		st8.start();
		semThread st9 = new semThread("I", sem);
		st9.start();
		semThread st10 = new semThread("J", sem);
		st10.start();
	}
}
