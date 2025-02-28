//Nitchayanin Thamkunanon 6580081
package Ex7_6580081;

import java.util.*;
import java.util.concurrent.*;

////////////////////////////////////////////////////////////////////////////////
class BankThread extends Thread
{
    private final Object lock; //for main thread to signal all threads
    private Account             sharedAccount, exchangingAccount;
    private Exchanger<Account>  exchanger; //exchange accounts between depositing BankThreads
    private CyclicBarrier       barrier; //make threads start some tasks at the same time
    private int                 rounds;
    private boolean             modeD;                  //deposit (true) or withdraw (false)
    public static int          simulation;

    public BankThread(String n, Account sa, boolean m, Object lock)  {
        super(n); sharedAccount = sa; modeD = m; this.lock = lock;
    }
    public void setBarrier(CyclicBarrier ba)            { barrier = ba; }
    public void setExchanger(Exchanger<Account> ex)     { exchanger = ex; }
    public void setRounds(int rounds){this.rounds = rounds;}
    public boolean getModeD(){return this.modeD;}
    public void run() {
        // Loop for banking simulations
        while(true)
        {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //  (1) Wait until main thread gets #rounds from user and pass it to BankThread.
            synchronized (lock)
            {
                try {
                    lock.wait();
                } catch (Exception e) {} //wait until main notifies all the thread to run again (when user insert rounds)
            }
            if(rounds == -1) break; //break if -1
            if (simulation != 1) //if not first run, exchange deposit threads' accounts
            {
                if (modeD) {
                    try {
                        sharedAccount = exchanger.exchange(sharedAccount); //EXCHANGE process (D1, D2)
                    }
                    catch (InterruptedException e) {
                    }
                    System.out.printf("%-4s >> "+"exchanging account\n",Thread.currentThread().getName());
                }
                try {
                    barrier.await(); //withdrawing threads are waiting at the barrier
                } catch (Exception e) {}
            }
            //identify before printing round 1,2,3...
            identifying();
            try {
                barrier.await();
            } catch (Exception e) {}

            //Depositing threads deposit to sharedAccount for #rounds.
            //Withdrawing threads withdraws from sharedAccount for #rounds
            for (int i = 1; i <= rounds; i++)
            {
                if (modeD) sharedAccount.deposit(i);
                if (!modeD) sharedAccount.withdraw(i);
                try {
                    barrier.await(); //wait for all threads to finish in each round
                } catch (Exception e) {}
            }
            //waiting for all threads to finish
            try {
                barrier.await(); //wait for all threads to finish in each round
            } catch (Exception e) {}
            //then notify main that all threads have finished, so that main can continue running the loop
            synchronized (lock) {lock.notifyAll();}
            // Break from loop & return if user doesn't want to run a new simulation (rounds = -1)
        }
    }
    public void identifying()
    {
        System.out.printf("%-4s" +" >> manage "+"%4s"+sharedAccount.getName() +"  (balance = "+ sharedAccount.getBalance()+")\n",Thread.currentThread().getName(),"");
    }
}

////////////////////////////////////////////////////////////////////////////////
/// buffer for synchronization
class Account {
    private String name;
    private int balance;

    public Account(String id, int b) {
        name = id;
        balance = b;
    }

    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }

    synchronized public void deposit(int i) {
        BankThread me = (BankThread) (Thread.currentThread());
        Random random = new Random();
        int randomMoney = random.nextInt(100) + 1;
        // Random money (1 to 100) to deposit; update the balance
        balance += randomMoney;
        report(i, randomMoney);// Report thread activity (see example output)
    }

    synchronized public void withdraw(int i) {
        BankThread me = (BankThread) (Thread.currentThread());
        Random random = new Random();
        if (balance == 0 || balance/2==0)//   - But if balance is already 0, report that withdrawal fails
            System.out.printf("%-4s"+ " >> round " + i + "%4s" + getName() + " cannot withdraw\n",me.getName(), "");
        else {
            int randomMoney = random.nextInt(balance / 2) +1; // Random between 1 and balance/2
            balance -= randomMoney;
            report(i, randomMoney);// Report thread activity (see example output)
        }
    }

    private void report(int round, int randMoney) {
        BankThread me = (BankThread) (Thread.currentThread());
        String money;
        if (me.getModeD()) {
            money = " +" + String.valueOf(randMoney);
        } else {
            money = " -" + String.valueOf(randMoney);
        }
        System.out.printf("%-4s"+ " >> round " + round + "%4s" + getName() + "%5s" + "%5s"+"%-8s"+" = %4d\n",me.getName(), "", money,"","balance",balance);
    }
    public void reportFinalBalance()
    {
        System.out.printf("%-4s"+ " >> final balance "+"%3s"+getName()+" = %4d\n",Thread.currentThread().getName(),"",balance);
    }
}

    /// /////////////////////////////////////////////////////////////////////////////
    public class Ex7_6580081 {
        public static void main(String[] args) {
            Ex7_6580081 mainApp = new Ex7_6580081();
            mainApp.runSimulation();
        }

        public void runSimulation() {
            int simulations = 0;
            Scanner keyboardScan = new Scanner(System.in);
            Account[] accounts = {new Account("account A", 0),
                    new Account(".".repeat(35) + "account B", 0)};

            //Creating threads, exchanger,lock(for main and threads to notify each other),barrier
            ArrayList<BankThread> allThreads = new ArrayList<>();
            Exchanger<Account> exchanger = new Exchanger<>();
            final Object lock = new Object();//for main to signal other threads (they need to synchronized on the same object)
            allThreads.add(new BankThread("D1", accounts[0], true,lock));
            allThreads.add(new BankThread("D2", accounts[1], true,lock));
            allThreads.add(new BankThread("W1", accounts[0], false,lock));
            allThreads.add(new BankThread("W2", accounts[1], false,lock));
            CyclicBarrier barrier = new CyclicBarrier(allThreads.size());

            // Pass Exchanger & CyclicBarrier objects (for deposit threads)
            for (BankThread T : allThreads) {
                T.setBarrier(barrier);
                T.setExchanger(exchanger);
            }
            //  Pass CyclicBarrier object (Exchanger can be set to null) (for withdrawal threads)
            for (int i = 2; i < allThreads.size(); i++) {
                allThreads.get(i).setBarrier(barrier);
            }
            // Start all BankThreads
            for (BankThread T : allThreads) {
                T.start();
            }
            int rounds = 0;
            //MAIN LOOP for banking simulation
            while (true) {
                System.out.println(Thread.currentThread().getName() + " >> Enter #rounds for a new simulation (-1 to quit)");
                rounds = keyboardScan.nextInt();//user enter rounds
                simulations++;
                BankThread.simulation = simulations; //set simulation counts for all threads
                //  (1) Main thread gets #rounds from user and pass it to BankThread.
                for (BankThread T : allThreads) {
                    T.setRounds(rounds); //pass rounds to BankThread
                }
                synchronized (lock) {
                    lock.notifyAll(); //main threads notify other threads to continue working
                }
                if(rounds==-1)break;
                //  (2) Main thread waits until all BankThread completes #rounds of deposit/withdraw (BankThread will send notification for main to continue the loop)
                synchronized (lock){
                    try {
                        lock.wait(); //main thread waits
                    } catch (Exception e) {}
                }
                System.out.println();
            }
            //main thread waits until all BankThreads return so that it can print reports
            try {
                for (BankThread T : allThreads) {
                    T.join();
                }
            } catch (InterruptedException e) {}
            //Main thread reports final balances of all accounts
            for(Account A : accounts)
            {
                A.reportFinalBalance();
            }
        }
    }

