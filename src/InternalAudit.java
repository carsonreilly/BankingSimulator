/* Name: Carson Reilly
Course: CNT 4714 Spring 2024
Assignment title: Project 2 – Synchronized, Cooperating Threads Under Locking
Due Date: February 13, 2024
*/

import java.util.Random;

public class InternalAudit implements Runnable{
	private static final int MAXSLEEP = 10000; //Creates integer that holds the maximum sleep time of 10000
	private TheBank bank;
	private String threadName; //Creates private variables for constructor use
	public InternalAudit(TheBank b,String name) {
		bank=b;
		threadName=name; //Sets TheBank object and thread name
	}
	public void run() { //Runs when function is called
        while (true) { //Infinite loop that only stops for sleep
            try {
                Thread.sleep(new Random().nextInt(MAXSLEEP)); //Sleeps for random amount of time
                bank.internalAudit(); //Calls the internal audit function to print audit information
            } catch (Exception e) {
                e.printStackTrace(); //Prints stack trace if there's an exception (Shouldn't run)
            }
        }
    }
}
