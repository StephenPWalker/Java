
public class BankAccountTest 
{
	public static void main(String[] args)
	{
		BankAccount myAccount = new BankAccount(0);
		System.out.println("Initial balance is " + myAccount.getBalance());
		myAccount.deposit(1000); //make initial deposit
		myAccount.withdraw(400); //remove 400
		myAccount.withdraw(400); //remove 400
		myAccount.withdraw(400); //remove 400 should provide a waiting time error due to too many transactions
		myAccount.withdraw(400); //remove 400 should return not enough funds
		System.out.println(myAccount); //should print out the current balance
	}
}
