/* Name: Carson Reilly
Course: CNT 4714 Spring 2024
Assignment title: Project 2 – Synchronized, Cooperating Threads Under Locking
Due Date: February 13, 2024
*/

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;

public class BankAccount implements TheBank{
	private int balance=0;
	private Lock lock=new ReentrantLock();
	private Condition canWrite = lock.newCondition();
	private int internalCounter=0;
	private int treasuryCounter=0;
	private int totalCounter=0; //We set the private variables needed for calculations and printing to 0 and create a lock that will be used to lock and unlock the classes and a second lock for writing priviledges
	
	public void flaggedTransaction (int balanceAmount, String name,String type)throws Exception {
		lock.lock(); //Function holds the lock
		String excelPath="FlaggedTransactions.csv"; //Sets path to write to csv for flagged transactions
		BufferedWriter bw=new BufferedWriter(new FileWriter(excelPath,true)); //Creates buffered writer that will append to the csv file
		Date currentDate=new Date(); //Gets the current date
		SimpleDateFormat timeFormat=new SimpleDateFormat("dd/MM/yyyy hh:mm:ssa z"); //Sets the time format that will be printed
		FileWriter f=new FileWriter("Transactions.txt",true); //Creates file writer that will write to the txt transaction file
		try {
			if(type.equals("w")) { //Checks to see if it is a withdrawal that is flagged 
				System.out.println("\n* * * Flagged Transaction - Withdrawal Agent "+name+" Made A Withdrawal In Excess of $"+balanceAmount+".00 USD - See Flagged Transaction Log.\n"); //Prints the flagged information to the console
				f.write("\n* * * Flagged Transaction - Withdrawal Agent "+name+" Made A Withdrawal In Excess of $"+balanceAmount+".00 USD - See Flagged Transaction Log.\n"); //Writes the flagged information to the transactions txt file
				bw.append("\nDepositor Agent "+name+" issued withdrawal of $"+balanceAmount+" at: "+timeFormat.format(currentDate)+" Transaction Number: "+totalCounter); //Appends the flagged information to the flagged transaction csv
				bw.close(); //Closes the buffered writer
				f.write("\n* * * Flagged Transaction - Withdrawal Agent "+name+" Made A Withdrawal In Excess of $"+balanceAmount+".00 USD - See Flagged Transaction Log.\n"); //Writes the flagged information to the transactions txt file
				f.close(); //Closes the file writer
			}
			if(type.equals("d")) { //Checks to see if it is a deposit that is flagged
				System.out.println("\n* * * Flagged Transaction - Depositor Agent "+name+" Made A Despoit In Excess of $"+balanceAmount+".00 USD - See Flagged Transaction Log.\n"); //Prints the flagged information to the console
				f.write("\n* * * Flagged Transaction - Depositor Agent "+name+" Made A Despoit In Excess of $"+balanceAmount+".00 USD - See Flagged Transaction Log.\n"); //Writes the flagged information to the transactions txt file
				bw.append("\nDepositor Agent "+name+" issued deposit of $"+balanceAmount+" at: "+timeFormat.format(currentDate)+" Transaction Number: "+totalCounter); //Appends the flagged information to the flagged transactions csv
				bw.close(); //Closes the buffered writer
				f.close();//Closes the file writer
			}
            try {
                
            } catch (Exception e) {
                e.printStackTrace(); //Prints the stack trace if an exception is caught (Should never run)
            }
        } finally {
            lock.unlock(); //Lets go of the lock
        }
	}
	public void deposit(int balanceAmount, String name) throws Exception {
		lock.lock(); //Function holds the lock
		FileWriter f=new FileWriter("Transactions.txt",true); //Sets the file writer to the transactions txt file
		try {
			balance+=balanceAmount; //Adds the deposit amount (accidentally named balanceAmount) to the private balance of the account
			internalCounter++; //Increments the internal audit counter by one for tracking
			treasuryCounter++; //Increments the treasury audit counter by one for tracking
			totalCounter++; //Increments the total counter by one for tracking
			canWrite.signalAll(); //Awakens sleeping threads
			if(balance<100) { //If statement done for formatting purposes
				System.out.println("Agent " + name + " deposits $" + balanceAmount + "\t\t\t\t\t\t\t\t\t        (+) Balance: " + balance+"\t\t\t\t\t\t\t\t"+totalCounter); //Prints the information of the deposit to the console
				f.write("Agent " + name + " deposits $" + balanceAmount + "\t\t\t\t\t\t\t\t\t        (+) Balance: " + balance+"\t\t\t\t\t\t\t\t"+totalCounter+"\n"); //Writes the information of the deposit to the transaction txt file
			}
			else {
				System.out.println("Agent " + name + " deposits $" + balanceAmount + "\t\t\t\t\t\t\t\t\t        (+) Balance: " + balance+"\t\t\t\t\t\t\t"+totalCounter); //Prints the information of the deposit to the console
				f.write("Agent " + name + " deposits $" + balanceAmount + "\t\t\t\t\t\t\t\t\t        (+) Balance: " + balance+"\t\t\t\t\t\t\t"+totalCounter+'\n'); //Writes the information of the deposit to the transactions txt file
			}
			if(balanceAmount>350) {
				flaggedTransaction(balanceAmount,name,"d"); //Calls the flagged transaction function to run if the deposit amount is more than 350, this will print the flagged transaction information 
			}
		}
		catch(Exception e) {
			System.out.println("Deposit blocked"); //Catch for when there is an Exception error (Shouldn't run)
		}
		finally {
			lock.unlock(); //Lets go of the lock
			f.close(); //Closes the file writer
		}
	}
	public void withdraw(int balanceAmount, String name) throws Exception{
	    lock.lock(); //Function acquires the lock
		FileWriter f=new FileWriter("Transactions.txt",true); //Sets file writer to transactions txt path
	    try {
	        while (balanceAmount > balance) { //Checks if too much is requested to withdraw
	            f.write("Agent " + name + " tries to withdraw too much\t\t\t\t\t\t\t\t(*******) WITHDRAWAL BLOCKED - INSUFFICIENT FUNDS!!!"+"\n"); //Writes withdrawal error to transactions txt file
	        	System.out.println("Agent " + name + " tries to withdraw too much\t\t\t\t\t\t\t\t(*******) WITHDRAWAL BLOCKED - INSUFFICIENT FUNDS!!!"); //Prints withdrawal error to console
	            canWrite.await();//Pauses/sleeps any other functions
	        }

	        balance -= balanceAmount;//Subtracts the withdrawal amount from the account balance
	        totalCounter++; //Increments total counter by 1
	        treasuryCounter++; //Increments treasury counter by 1
	        internalCounter++; //Increments internal counter by 1
	        if(balance<100&&balanceAmount<10) { //If statement for handling formatting
		        System.out.println("\t\t\t\t\t  Agent " + name + " withdrawals $" + balanceAmount + "\t\t\t\t(-) Balance: " + balance + "\t\t\t\t\t\t                " + totalCounter); //Prints the withdrawal information to the console
		        f.write("\t\t\t\t\t  Agent " + name + " withdrawals $" + balanceAmount + "\t\t\t\t(-) Balance: " + balance + "\t\t\t\t\t\t                " + totalCounter+"\n"); //Writes the withdrawal information to the transactions txt file
	        }
	        else if(balance<100) {
		        System.out.println("\t\t\t\t\t  Agent " + name + " withdrawals $" + balanceAmount + "\t\t\t\t(-) Balance: " + balance + "\t\t\t\t\t\t                " + totalCounter);//Prints the withdrawal information to the console
		        f.write("\t\t\t\t\t  Agent " + name + " withdrawals $" + balanceAmount + "\t\t\t\t(-) Balance: " + balance + "\t\t\t\t\t\t                " + totalCounter+"\n");//Writes the withdrawal information to the transactions txt file
	        }
	        else if(balanceAmount>10) {
		        System.out.println("\t\t\t\t\t  Agent " + name + " withdrawals $" + balanceAmount + "\t\t\t\t(-) Balance: " + balance + "\t\t\t\t\t                " + totalCounter);//Prints the withdrawal information to the console
		        f.write("\t\t\t\t\t  Agent " + name + " withdrawals $" + balanceAmount + "\t\t\t\t(-) Balance: " + balance + "\t\t\t\t\t                " + totalCounter+"\n");//Writes the withdrawal information to the transactions txt file
	        }
	        else {
		        System.out.println("\t\t\t\t\t  Agent " + name + " withdrawals $" + balanceAmount + "\t\t\t\t(-) Balance: " + balance + "\t\t\t\t\t                " + totalCounter);//Prints the withdrawal information to the console
		        f.write("\t\t\t\t\t  Agent " + name + " withdrawals $" + balanceAmount + "\t\t\t\t(-) Balance: " + balance + "\t\t\t\t\t                " + totalCounter+"\n");//Writes the withdrawal information to the transactions txt files
	        }

	        if (balanceAmount >= 75) { //Checks if withdrawal amount is greater than or equal to 75
	            flaggedTransaction(balanceAmount, name,"w"); //Calls the flaggedTransaction function to print flagged withdrawal information
	        }
	    } catch (Exception e) {
	        e.printStackTrace(); //Prints stack trace in case of error (Shouldn't run)
	    } finally {
	        lock.unlock(); //Lets go of the lock
	        f.close(); //Closes the file writer
	    }
	}

	public void internalAudit() throws Exception{
		lock.lock(); //Function acquires the lock
		FileWriter f=new FileWriter("Transactions.txt",true); //Creates file writer to transactions txt file 
        try {
            System.out.println("Internal Bank Audit Finds Current Balance To Be $" + balance +"\t\t\t\tNumber Of Transactions Since Last Audit " + internalCounter); //Prints the internal audit information to the console
            f.write("Internal Bank Audit Finds Current Balance To Be $" + balance +"\t\t\t\tNumber Of Transactions Since Last Audit " + internalCounter+"\n"); //Writes the internal audit information to the transactions txt file
            internalCounter = 0; // Resets the internalAudit counter to 0 for tracking
        } finally {
            lock.unlock(); //Lets go of the lock
            f.close(); //Closes the file writer
        }
	}
	public void treasuryAudit() throws Exception{
		lock.lock(); //Function acquires the lock
		FileWriter f=new FileWriter("Transactions.txt",true); //Creates file writer to the transactions txt file
		try {
			System.out.println("Treasury Bank Audit Finds Current Balance To Be $"+balance+"\t\t\t\tNumber of Transactions Since Last Audit "+treasuryCounter); //Prints treasury audit information to the console
			f.write("Treasury Bank Audit Finds Current Balance To Be $"+balance+"\t\t\t\tNumber of Transactions Since Last Audit "+treasuryCounter+"\n"); //Writes treasury audit information to the transactions txt file
			treasuryCounter=0; //Resets the treasuryCounter to 0 for tracking
		}finally {
			lock.unlock(); //Lets go of the lock
			f.close(); //Closes the file writer
		}
	}
}
