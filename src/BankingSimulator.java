/* Name: Carson Reilly
Course: CNT 4714 Spring 2024
Assignment title: Project 2 – Synchronized, Cooperating Threads Under Locking
Due Date: February 13, 2024
*/


import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BankingSimulator {
    public static final int MAX_AGENTS = 17; //Sets the max number of threads we want

    public static void main(String[] args) throws IOException {
        ExecutorService app = Executors.newFixedThreadPool(MAX_AGENTS); //Creates thread pool with max number of agents
        BankAccount account = new BankAccount(); //Creates bank account object for simulating
		FileWriter f=new FileWriter("Transactions.txt",true); //Creates file writer to the transaction txt file
        try {
            System.out.println("* * *   SIMULATION BEGINS...\n");
            f.write("* * *   SIMULATION BEGINS...\n");
            System.out.println("Depositor Agents\t\t\t   Withdrawal Agents\t\t\t\t\t       Balance                                                     Transaction Number");
            f.write("Depositor Agents\t\t\t   Withdrawal Agents\t\t\t\t\t       Balance                                                     Transaction Number"+"\n");
            System.out.println("----------------\t\t\t   -----------------\t\t\t\t\t------------------------\t\t\t\t\t--------------------------");
            f.write("----------------\t\t\t   -----------------\t\t\t\t\t------------------------\t\t\t\t\t--------------------------"+"\n"); //Lines 16-21 Print and write the beginning formatting to the console and transaction txt file
            ArrayList<Integer> threadNumbers = new ArrayList<Integer>();  //Creates Array List to hold the thread ID
    	    Random randomGenerator = new Random(); //Creates Random object
    	    f.close(); //Closes file writer
    	    while (threadNumbers.size() < 10) {//Loops through first 10 elements in the thread numbers array list
    		    int random = randomGenerator .nextInt(10); //Assigns integer to random number 1-10
    		    if (!threadNumbers.contains(random)) { //Checks to make sure random number hasn't been used yet
    		        threadNumbers.add(random); //Adds random number to array list
    		    }
    	    }
		
    		try {			 
    			    for (int i = 1; i < threadNumbers.size(); i++) //Loops through array list size
    			    {
    			    	int idNum=threadNumbers.get(i)+1; //Creates integer that hold the ID number +1 (so it starts at 1, instead of 0)
    			    	app.execute( new Withdrawal( account, "W" + idNum)); //Creates withdrawal agent in the bank account with the name W#
    					if(threadNumbers.get(i) < 5) //Checks to see if the array list value is less than 5
    						app.execute( new Depositor( account, "D" + idNum) ); ////Creates depositor agent in the bank account with the name D#
    			    }
    						
    			}
    		catch(Exception exception)
    		{
    			exception.printStackTrace();//Prints stack trace if there's an exception (Shouldn't run)
    			
    		}

            app.execute(new InternalAudit(account, "INTERNAL BANK AUDITOR")); //Creates internal audit agent in bank account with name "INTERNAL BANK AUDITOR"
            app.execute(new TreasuryAudit(account, "TREASURY BANK AUDITOR")); //Creates treasury audit agent in bank account with name "TREASURY BANK AUDITOR"
        } catch (Exception e) {
            e.printStackTrace(); //Prints stack trace if there's an exception (Shouldn't run)
        } finally {
            app.shutdown(); //Shuts down thread pool
        }
    }
}
