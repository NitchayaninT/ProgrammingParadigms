package Lab_Ch6;
class Human
{
    private String name;
    public Human(String n)                  { name = n; }
    public String getName()                 { return name; }
};
//cannot extend class Thread (if a class already inherit from another class, java doesnt not allow multiple inheritance so we have to implement an interface)
class MySoul extends Human implements Runnable
{
    public MySoul(String n)                 { super(n); }
    public String activity()                { return ("runnable activity"); }

    @Override
    public void run() //able to call the method in Thread (which is "run()")
    {
	// ----- (1) call thread's methods in runnable object
	Thread me = Thread.currentThread();
	try  { me.sleep(200); }  catch (InterruptedException e)  { }
	System.out.printf("Thread = %-8s   runnable = %-4s   -->   run in %s \n\n", 
                          me.getName(), getName(), getClass().getName());
    }
};

//////////////////////////////////////////////////////////////////////////////////////////////////
class MyBody extends Thread  
{
    private MySoul soul;	
    public MyBody(MySoul s, String n)       { super(s, n); soul = s; }
    
    @Override
    public void run()  //overides run
    {
	// ----- (2) call runnable's methods in thead object
	System.out.printf("Thread = %-8s   runnable = %-4s   -->   run in %s ", 
                          getName(), soul.getName(), getClass().getName());
	System.out.printf("   -->   call %s \n\n", soul.activity());
    }
    //but not necessary to override run(), it can invoke run() that belongs to Runnable (Runnable has run() method)
}

class HerBody extends Thread //can extend thread because class HerBody doesnt inherit from anyone
{
    private MySoul soul; //MySoul implements Runnable
    public HerBody(MySoul s, String n)      { super(s, n); soul = s; }//call blank method
}

//////////////////////////////////////////////////////////////////////////////////////////////////
class w6_4_HumanThread
{
    public static void main(String[] args) 
    {
	Thread tbody  = new Thread( new MySoul("TSoul"), "Thread" ); //this one implements Runnable.
	MyBody mbody  = new MyBody( new MySoul("MSoul"), "MyBody" ); //MyBody overrides run(), useless to override run here. look at HerBody as examaple
	HerBody hbody = new HerBody( new MySoul("HSoul"), "HerBody" );//HerBody doesn't override run().Calling runnable methods in thread
        
        tbody.start();  mbody.start();  hbody.start();
    }
}
