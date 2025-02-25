// Don't forget to rename the package
package Ex7;    

import java.util.*;
import java.util.concurrent.*;

////////////////////////////////////////////////////////////////////////////////
class BankThread extends Thread
{
    private Account             sharedAccount;    
    private Exchanger<Account>  exchanger;         
    private CyclicBarrier       barrier;
    private int                 rounds;
    private boolean             modeD;                  //deposit (true) or withdraw (false)

    public BankThread(String n, Account sa, boolean m)  { 
        super(n); sharedAccount = sa; modeD = m;
    }
    public void setBarrier(CyclicBarrier ba)            { barrier = ba; }
    public void setExchanger(Exchanger<Account> ex)     { exchanger = ex; }
    
    public void run() {
        // Loop for banking simulation. In each simulation:        
        //  (1) Wait until main thread gets #rounds from user and pass it to BankThread.
        
        //  (2) If this is the first simulation, skip this step.
        //      Otherwise, depositing threads exchange accounts with each other.
        //      Withdrawing threads that don't exchange accounts must wait until this is done.
        
        //  (3) Each thread identifies account it is managing in this simulation
        
        //  (4) Depositing threads deposit to sharedAccount for #rounds.
        //      Withdrawing threads withdraws from sharedAccount for #rounds
        
        // Break from loop & return if user doesn't want to run a new simulation
    }
}

////////////////////////////////////////////////////////////////////////////////
class Account {
    private String  name;
    private int     balance;
    
    public Account(String id, int b)   { name = id; balance = b; }
    public String getName()            { return name; }
    public int    getBalance()         { return balance; }
    
    public void deposit() 
    {
        // Random money (1 to 100) to deposit; update the balance
        // Report thread activity (see example output)
    }
    
    public void withdraw()
    {
        // Random money (1 to balance/2) to withdraw; update the balance
        // Report thread activity (see example output)
        //   - But if balance is already 0, report that withdrawal fails
    }
}

////////////////////////////////////////////////////////////////////////////////
public class Ex7 {
    public static void main(String[] args) {
        Ex7 mainApp = new Ex7();
        mainApp.runSimulation();
    }

    public void runSimulation()
    {    
        Scanner keyboardScan = new Scanner(System.in);
        Account [] accounts  = { new Account("account A", 0), 
                                 new Account(".".repeat(35) + "account B", 0) };   
         
        ArrayList<BankThread> allThreads = new ArrayList<>();
        Exchanger<Account> exchanger     = new Exchanger<>();
        CyclicBarrier barrier            = new CyclicBarrier( allThreads.size()+1 );
        
        // Create BankThread D1 and D2 for deposit task
        //   - In the first simulation, D1 manages account A, D2 manages accout B
        //   - Pass Exchanger & CyclicBarrier objects
        
        // Create BankThread W1 and W2 for withdraw task
        //   - In all simulations, W1 manages only account A, W2 manages only account B
        //   - Pass CyclicBarrier object (Exchanger can be set to null)

        
        // Start all BankThreads

        
        // Loop for banking simulation. In each simulation:
        //  (1) Main thread gets #rounds from user and pass it to BankThread.
        //  (2) Main thread waits until all BankThread completes #rounds of deposit/withdraw
        
        
        // If user dosn't want to run a new simulation:
        //   - Wait until all BankThreads return
        //   - Main thread reports final balances of all accounts
    }
}
