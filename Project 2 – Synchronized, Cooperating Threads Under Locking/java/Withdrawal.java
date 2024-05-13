
/*  Name: Victoria Ten  
	Course: CNT 4714 Spring 2024 
	Assignment title: Project 2 – Synchronized, Cooperating Threads Under Locking 
	Due Date: February 11, 2024 
*/ 

package project2;

import java.util.Random;

public class Withdrawal implements Runnable {

	
	private static final int MAX_WITHDRAWAL = 99;
	private static final int MAXSLEEPTIME = 1000;
	private TheBankAccount account;
    private Random random;
    String thread_name;
	
	public Withdrawal(TheBankAccount account, String name) {
		
		this.account = account;
        this.random = new Random();
        thread_name = name;
	}

	public void run() {
		while(true) { 
			try { 
				Thread.sleep(random.nextInt(MAXSLEEPTIME)+1); 
                int withdrawalAmount = random.nextInt(MAX_WITHDRAWAL) + 1;
                account.withdraw(withdrawalAmount, thread_name);
			} 
			catch(Exception e) {
				System.out.print("Exception throw withdrawing !");
			}
		}
	}
	
}
