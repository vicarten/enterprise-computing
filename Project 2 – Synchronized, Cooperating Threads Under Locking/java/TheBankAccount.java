
/*  Name: Victoria Ten  
	Course: CNT 4714 Spring 2024 
	Assignment title: Project 2 – Synchronized, Cooperating Threads Under Locking 
	Due Date: February 11, 2024 
*/ 
package project2;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TheBankAccount implements TheBank{


	private int balance;
    private int transactionNumber;
    private Lock lock;
    private int intAuditCount = 0;
    private int depAuditCount = 0;
	
    public TheBankAccount() {
        this.balance = 0;
        this.transactionNumber = 1;
        this.lock = new ReentrantLock();
    }
   
	public void flagged_transaction(int amount, String threadName, String threadType) {
		
		PrintWriter writer = null;
		
		try {
	
			writer = new PrintWriter(new FileWriter("transactions.csv", true));

			
	        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	        String timestamp = sdf.format(new Date());

	        StringBuilder transactionString = new StringBuilder();
	 

	        
			if (threadType.equals("D")) 
			{	
			
				transactionString.append("Depositor ").append(threadName).append(" issued deposit of $").append(amount).append(".00 at: ");
				transactionString.append(timestamp);
				transactionString.append(" EST\tTransaction Number : ").append(transactionNumber-1).append("\n\n");
				writer.print(transactionString.toString());
				System.out.printf("\n***Flagged Transaction - Depositor %s Made A Deposit In Excess Of $350.00 USD - See Flaged Transaction Log.\n\n",
	                    threadName);
	        } else {
	        	
	        	transactionString.append("\tWithdrawal ").append(threadName).append(" issued withdrawal of $").append(amount).append(".00 at: ");
				transactionString.append(timestamp);
				transactionString.append(" EST\tTransaction Number : ").append(transactionNumber-1).append("\n\n");
				writer.print(transactionString.toString());
	            System.out.printf("\n***Flagged Transaction - Withdrawal %s Made A Withdrawal In Excess Of $75.00 USD - See Flaged Transaction Log.\n\n",
	                    threadName);
	        }
		}
		catch(IOException ioException) {
			System.out.println("\nError: Problem writing to transaction file.\n");
		}
		finally
		{
			
			if (writer != null) {
	            writer.close();
	        }
		}
	}
	
	
   
	public void deposit(int amount, String threadName) {
		
		lock.lock();
		try {
			
			 balance += amount;
			
			 System.out.printf("%s deposits $%d\t\t\t\t\t\t\t(+) Balance is $%d. \t\t\t\t\t %d\n",
				 threadName, amount, balance, transactionNumber++);
			 if (amount > 350) {
				 flagged_transaction(amount, threadName, "D");
			 }
			
			
		}
		catch(Exception e) {
			System.out.print("Exception throw depositing");
		}
		finally
		{
			
			lock.unlock();
		}
	}
	
	
    
	public void withdraw(int amount, String threadName) {
		
		lock.lock();
		
		try {
			
			if (balance >= amount) {
	            balance -= amount;
	            System.out.printf("\t\t\t\t %s withdraws $%d \t\t(-) Balance is $%d. \t\t\t\t\t %d\n",
	                    threadName, amount, balance, transactionNumber++);
	            if (amount > 75) {
	            	flagged_transaction(amount, threadName, "W");
	            }
	        } else {
	            System.out.printf("\t\t\t\t %s withdraws $%d \t\t(******) WITHDRAWAL BLOCKED - INSUFICIENT FUNDS!!!\n",
	                    threadName, amount, balance);
	        }
		}
		catch(Exception e) {
			System.out.print("An Exception was thrown getting the withdrawal.");
		}
		finally
		{
			
			lock.unlock();
		}
	}
		
		
		
    
	public void internalAudit() {
		
    	lock.lock();
		try {
			System.out.printf("\n\n******************************************************************************************************************************************************************\n\n");
			System.out.printf("\t\tINTERNAL BANK AUDITOR FINDS CURRENT ACCOUNT BALANCE TO BE: $%d\t Number of transactions since last Internal audit is: %d\n",
                    balance, transactionNumber - intAuditCount-1);
			System.out.printf("\n\n******************************************************************************************************************************************************************\n\n");
			intAuditCount = transactionNumber-1;
			
		}
		catch(Exception e) {
			System.out.print("An Exception was thrown getting the balance by the Auditor.");
		}
		finally
		{
			
			lock.unlock();
		}
	}

	
	
	
    
	public void treasuryAudit() {
	
    	lock.lock();
		try {
			
			System.out.printf("\n\n******************************************************************************************************************************************************************\n\n");
			System.out.printf("\t\tTREASURY DEPT AUDITOR FINDS CURRENT ACCOUNT BALANCE TO BE: $%d\t Number of transactions since last Treasury audit is: %d\n",
                    balance, transactionNumber - depAuditCount-1);
			System.out.printf("\n\n******************************************************************************************************************************************************************\n\n");
			depAuditCount = transactionNumber-1;
		}
		catch(Exception e) {
			System.out.print("An Exception was thrown getting the balance by the Auditor.");
		}
		finally
		{
			
			lock.unlock();
		}
	}
	
}
