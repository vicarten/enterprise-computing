
/*  Name: Victoria Ten  
	Course: CNT 4714 Spring 2024 
	Assignment title: Project 2 – Synchronized, Cooperating Threads Under Locking 
	Due Date: February 11, 2024 
*/ 

package project2;

public interface TheBank {

	public abstract void deposit(int amount, String threadName);
	
	public abstract void withdraw(int amount, String threadName);

	
	public abstract void flagged_transaction(int amount, String threadName, String threadType);
	
	public abstract void internalAudit();
	
	public abstract void tresuryAudit();

}
