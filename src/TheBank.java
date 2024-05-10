
/* Name: Carson Reilly
Course: CNT 4714 Spring 2024
Assignment title: Project 2 – Synchronized, Cooperating Threads Under Locking
Due Date: February 13, 2024
*/

public interface TheBank { //interface is used to declare all of the main functions that the bank account will use
	public abstract void flaggedTransaction(int counter, String name,String type) throws Exception;
	public abstract void deposit(int balance, String name) throws Exception;
	public abstract void withdraw(int balance, String name) throws Exception;
	public abstract void internalAudit() throws Exception;
	public abstract void treasuryAudit() throws Exception;
}
