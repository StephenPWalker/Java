import javax.swing.JOptionPane;

public class BankAccount 
{
	private float Balance;
	private int attempts = 0;
	/*
	 * Set balance
	 */
	BankAccount(float Balance)
	{
		this.Balance = Balance;
	}
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return "The current balance is £" + getBalance();
	}
	/*
	 * Return balance
	 */
	public float getBalance()
	{
		return Balance;
	}
	/*
	 * Deposit money (add)
	 */
	public void deposit(float depositAmount)
	{
		Balance = getBalance();
		Balance += depositAmount;
		System.out.println("Depositing £" + depositAmount);
		System.out.println(toString());
	}
	/*
	 * Withdraw money (subtract)
	 */
	public void withdraw(float withdrawAmount)
	{
		Balance = getBalance();
		attempts += 1;
		if(attempts < 3) 
		{
			System.out.println("Withdrawing £" + withdrawAmount);
			if(withdrawAmount > Balance) //not enough funds
			{
				String st = "Insufficient funds!";
				JOptionPane.showMessageDialog(null, st);
			}
			else
				Balance -= withdrawAmount;
			System.out.println(toString());
		}
		else // error when too many attempts occur
		{
			String st = "Too many attempts!";
			JOptionPane.showMessageDialog(null, st);
			resetAttempts();
		}
	}
	/*
	 * Delay / reset withdraw attempts
	 */
	private void resetAttempts()
	{
		try 
		{
			Thread.sleep(2000);
			attempts = 0;
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
}
