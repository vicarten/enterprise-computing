
/*  Name: Victoria Ten  
	Course: CNT 4714 Spring 2024 
	Assignment title: Project 2 – Synchronized, Cooperating Threads Under Locking 
	Due Date: February 11, 2024 
*/ 

package project2;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ABankingSimulator {

	public static final int MAX_AGENTS = 17; 
	public static final int NUM_DEPOSITORS = 5;
    public static final int NUM_WITHDRAWALS = 10;
    public static final int NUM_INT_AUDITOR = 1;
    public static final int NUM_DEP_AUDITOR = 1;
	public static void main(String[] args) {
	
	File file = new File("transactions.csv");
	if (file.exists()) {
	    file.delete();
	}
    
    ExecutorService application = Executors.newFixedThreadPool(MAX_AGENTS);

    TheBankAccount jointAccount = new TheBankAccount(); 
    try {
        System.out.print("* * *  SIMULATION BEGINS...");
        System.out.println();
        System.out.println("Deposit Agents\t\t\t Withdrawal Agents\t\t\t Balance\t\t\t\t\tTransaction Number");
        System.out.println("--------------\t\t\t -----------------\t\t\t---------\t\t\t\t\t------------------");

       
        for (int i = 1; i <= NUM_DEPOSITORS; i++) {
            application.execute(new Depositor(jointAccount, "Agent DT" + i));
        }

        
        for (int i = 1; i <= NUM_WITHDRAWALS; i++) {
            application.execute(new Withdrawal(jointAccount, "Agent WT" + i));
        }

        
        for (int i = 1; i <= NUM_INT_AUDITOR; i++) {
            application.execute(new InternalAuditor(jointAccount, "Internal Bank Auditor " + i));
        }
        
        for (int i = 1; i <= NUM_DEP_AUDITOR; i++) {
            application.execute(new TreasuryDeptAuditor(jointAccount, "Treasury Department Auditor " + i));
        }

    } catch (Exception exception) {
        exception.printStackTrace(); 
    }

        application.shutdown(); 
    }
}