
/*  Name: Victoria Ten  
	Course: CNT 4714 Spring 2024 
	Assignment title: Project 2 – Synchronized, Cooperating Threads Under Locking 
	Due Date: February 11, 2024 
*/ 

package project2;

import java.util.Random;

public class TreasuryDeptAuditor implements Runnable {

	private static final int MAX_SLEEP_TIME = 8000;
    private TheBankAccount account;
    private Random random;
    String thread_name;
	
	public TreasuryDeptAuditor(TheBankAccount account, String name) {
		
		this.account = account;
        this.random = new Random();
        thread_name = name;
	}

	public void run() {
		while(true) { 
			try { 
				Thread.sleep(random.nextInt(MAX_SLEEP_TIME) + 1); 
                account.treasuryAudit();
				
			} 
			catch(Exception e) {
				System.out.print("Exception throw withdrawing !");
			}
		}
	}
	
}