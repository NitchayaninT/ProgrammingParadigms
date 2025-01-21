package Lab_Ch4;

class Alien2
{
    /*static private final int maxcount = 4;
    static public int count = 0; */
    static public String planet = "Mars";

    // ----- (4) use class constructor to initialize static members

    static private final int maxcount;
    static public int count;

    static public void startup()                { System.out.println("\n=== Startup ===\n"); }
    //you can assign the values in static block (it has static vars and methods)
    static {
	maxcount = 4;
	count = 0;
	startup();
    }


    private String  name;

    // ----- (1) Object constructors - constructor chain
    private Alien2()				{ super(); System.out.print("Alien "); }//cannot call this outside class
    public Alien2(String n)			{ this(); name = n; System.out.println(name);}
//this() means this constructor Alien2 (without creating an object)
    // ----- (2) static & non-static members
    public String getName()                     { return name; }
    //static public String getName()            { return name; }//error. because name is not static, so we cannot declare like static for returning non static value
    static public String getPlanet()            { return planet; }

    static public boolean arrive()
    {
	if (count < maxcount) return true;
	else return false;
    }

    // ----- (5) update final variable
    //static public void moreAliens(int m)		{ maxcount = m; }
};

/////////////////////////////////////////////////////////////////////////////////////////////
class w4_2_Members extends java.lang.Object
{
    public w4_2_Members(){super();}; //DEFAULT CONSTRUCTOR. and by default, java constructor has to call another constructor, so super is called
    public static void main(String[] args) 
    {
        System.out.println("========== Program start ==========");
	String names[] = {"A", "B", "C", "D", "E", "F"};
	Alien2  a[]    = new Alien2[6];//just pointers to the object (not created an object yet)
    //class Alien2 will call staic block for declaration once creating Alien2 object
	for (int i=0; i < a.length; i++)
	{
            // ----- (1) constructor
            a[i] = new Alien2(names[i]); //create an object for each member in array
            
            if (i == 2) Alien2.planet = "Jupiter";

            
            // ----- (2) call static member "arrive()" via class name
            //           notice that getName() must be called via object only			
            if (Alien2.arrive())//can call static method (static member) by using class name and call directly
            {
                System.out.printf("***** %s welcomed at %s \n\n", a[i].getName(), Alien2.getPlanet());
		        //System.out.printf("***** %s welcomed at %s \n\n", Alien2.getName(), Alien2.getPlanet());//error because getname is non static, so you have to use object.method not class.method
		        Alien2.count++;//class name dot static method
            }
            

            /*
            // ----- (3) call static member "arrive()" via object
            if (a[i].arrive())
            {
                System.out.printf("***** %s welcomed at %s \n\n", a[i].getName(), a[i].getPlanet());
		a[i].count++;
            }
            */
	}

	// ----- (5) update final variable
	//Alien2.moreAliens(10);

	System.out.println();		
    }
}
