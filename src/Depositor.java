
/* Name: Carson Reilly
Course: CNT 4714 Spring 2024
Assignment title: Project 2 – Synchronized, Cooperating Threads Under Locking
Due Date: February 13, 2024
*/


import java.util.Random;

public class Depositor implements Runnable{
	private TheBank bank; //We set the TheBank object in order to successfully call the depositor class
	private static final int MAX_DEPOSIT=500; //setting the maximum deposit allowed
	private static final int MAXSLEEPTIME=4000;  //setting the maximum sleep time
	private String threadName; //setting thread name to successfully call the depositor class
	private static Random num = new Random(); //Setting a random object for sleeping and depositing
	public Depositor(TheBank b, String name) {
		bank=b;
		threadName=name; //Constructor that takes in a TheBank object (b) and String (name) and saves them to use in the class
	}
	public void run() { //run will make this run when it gets called
		while(true) { //endless loop that will only stop when sleep occurs
			try {
				bank.deposit(num.nextInt(MAX_DEPOSIT-1)+1,threadName); //Deposits a random number from 1-500 using the Random num object and the thread name (so it can be printed later)
				Thread.sleep(num.nextInt(MAXSLEEPTIME)); //Sleeps for a random amount of time between 1-4000
			}
			catch(Exception e) {
				System.out.print("Exception thrown depositing !"); //Prints when an exception is caught
			}
		}
	}
}
