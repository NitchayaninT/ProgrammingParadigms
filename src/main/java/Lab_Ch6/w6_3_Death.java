package Lab_Ch6;

class MyDeadThread extends Thread
{
    private boolean killed;

    MyDeadThread(String name) 
    { 
        super(name);
        killed = false;
        start(); //start the thread
    }

    public void kill()		{ killed = true; }

    @Override
    public void run()
    {
        int i = 1;
        
        // ----- (1) prepare for unexpected death
	/*try
	{            
            while (i <= 900)
            {  
                System.out.printf("%4d ", i); i++;
                if (i%10 == 0)
                    try {
                        sleep(10);
                    }  catch (InterruptedException e) { }
                
                // ----- (1) let thread kill itself !!!
                //if (i == 500) throw new ThreadDeath(); //when i is 500, throw threadDeath
            }
	}
	catch (ThreadDeath e)
	{
            System.out.println("\n\n***** clean up task (ThreadDeath)");
            throw e;
	}    */
        
        
        // ----- (3) use program logic instead of using try catch like above

        while (i <= 900 && !killed)
        {  
            System.out.printf("%4d ", i); i++; 
            if (i%10 == 0)
                try { sleep(10); }  catch (InterruptedException e) { }
        }

        
        //thread will not execute this (at first) because thread kills itself and exception is thrown from this method earlier
        //CLEANUP TASk
        if (!killed)
            System.out.println("\n\n***** " + getName() + " dies normally");
        else
            System.out.println("\n\n***** clean up task (program logic)");
    }
};

//////////////////////////////////////////////////////////////////////////////////////////////////
class w6_3_Death
{
    public static void main(String[] args) 
    {
	MyDeadThread T1 = new MyDeadThread("T1");

	try { Thread.sleep(100); }  catch (InterruptedException e) { }

	// ----- (2) stop thread (deprecated method)
        T1.stop(); //kill thread T1. means allowing anyone can kill thread, not good for practice cuz it might affect other threads
        //T1 dies normally gets printed, it wont throw new ThreadDeath exception

	// ----- (3) stop thread without using explicit stop() --> safer
        //T1.kill();

        // ----- (4) busy waiting (not good)
        while (T1.isAlive()) { }
        System.out.println("\n\n***** T1 is dead");	
    }
}
