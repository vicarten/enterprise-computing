
/*  Name: Victoria Ten  
	Course: CNT 4714 Spring 2024 
	Assignment title: Project 2 – Synchronized, Cooperating Threads Under Locking 
	Due Date: February 11, 2024 
*/ 

package project2;

import java.util.Random;

public class Depositor implements Runnable {

	
	private static final int MAX_DEPOSIT = 500;
	private static final int MAXSLEEPTIME = 2000;
	
	private TheBankAccount account;
    private Random random;
    String thread_name;
	
	public Depositor(TheBankAccount account, String name) {
		this.account = account;
        this.random = new Random();
        thread_name = name;
	}

	
	public void run() {
		while(true) { 
			try { 
				
				Thread.sleep(random.nextInt(MAXSLEEPTIME)+1);
				
				int depositAmount = random.nextInt(MAX_DEPOSIT) + 1;
                account.deposit(depositAmount, thread_name);
			} 
			catch(InterruptedException exception) {
				System.out.print("Exception throw depositing !");
			}
		}
	}

}
