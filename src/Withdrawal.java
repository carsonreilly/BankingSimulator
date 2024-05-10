/* Name: Carson Reilly
Course: CNT 4714 Spring 2024
Assignment title: Project 2 – Synchronized, Cooperating Threads Under Locking
Due Date: February 13, 2024
*/

import java.util.Random;

public class Withdrawal implements Runnable{
	private static Random num=new Random(); //Creates random object for sleep times and withdrawal amounts
	private TheBank bank;
	private String threadName; //Creates private variables for constructor
	private static final int MAX_WITHDRAW=99;
	private static final int MAXSLEEP=1500; //Creates private variables to hold max withdrawal amount and max sleep time
	public Withdrawal(TheBank b,String name) {
		bank=b;
		threadName=name; //Sets the TheBank value and name of the thread
	}
	public void run() { //Runs when function is called
		while(true) { //Infinite loop that only stops with sleep
			try {
				bank.withdraw(num.nextInt(MAX_WITHDRAW-1)+1, threadName); //Calls withdraw function with random number between 1-99 and the thread name
				Thread.sleep(num.nextInt(MAXSLEEP));//Sleeps a random amount of time between 1-1500
			}
			catch(Exception e) {
				System.out.println("Exception thrown withdrawing !"); //Prints stack trace if there's an exception (Shouldn't run)
			}
		}
	}
}
