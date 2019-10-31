
public class DeadlockMain 
{

	public static Object lock1 = new Object();
	public static Object lock2 = new Object();
	public static Object lock3 = new Object();
	public static Object lock4 = new Object();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ThreadClass t1 = new ThreadClass(lock1, lock2, lock3, lock4, 1);
		t1.start();
		ThreadClass t2 = new ThreadClass(lock1, lock2, lock3, lock4, 2);
		t2.start();
		ThreadClass t3 = new ThreadClass(lock1, lock2, lock3, lock4, 3);
		t3.start();
		ThreadClass t4 = new ThreadClass(lock1, lock2, lock3, lock4, 4);
		t4.start();
	}

}
