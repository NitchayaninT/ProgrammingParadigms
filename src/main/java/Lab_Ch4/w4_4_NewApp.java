package Lab_Ch4;
//for PreviousApp_1.java : to reuse a previous programming logic in new file, you can only call main method (not good)
public class w4_4_NewApp 
{
    public static void main(String[] args) 
    {
        // Reuse applicaion logic from previous program
        
        // ----- (1) Previous program has only main method
        /*
        System.out.println("----- Call PreviousApp (1) -----");
        w4_3_PreviousApp_1.main(args);
        */
        
        
        // ----- (2) Previous program has static methods. better but not the best

        /*System.out.println("----- Call PreviousApp (2) -----");
        System.out.println("\n\n+++++ Reuse 1 : default bounds ");
        w4_3_PreviousApp_2.task(); //use class name to call method task (to reuse the previous program logic)
        
        System.out.println("\n\n+++++ Reuse 2 : new bounds ");
        w4_3_PreviousApp_2.setBounds(6, 8);//change the value of static vars
        w4_3_PreviousApp_2.task();

        System.out.println("\n\n+++++ Reuse 3 : back to default bounds ");
        w4_3_PreviousApp_2.setBounds(5, 10);
        w4_3_PreviousApp_2.task(); */

        
        //best method : try to make methods non static ***
        //try to make vars private, not public
        // ----- (3) Previous program has non-static methods

        System.out.println("----- Call PreviousApp (3) -----");  
        System.out.println("\n\n+++++ Reuse 1 : default bounds ");
        w4_3_PreviousApp_3 reuse1 = new w4_3_PreviousApp_3(); //create an object to use non static method
        reuse1.task(); //use object to call method
        
        System.out.println("\n\n+++++ Reuse 2 : new bounds ");
        w4_3_PreviousApp_3 reuse2 = new w4_3_PreviousApp_3();
        reuse2.setBounds(6, 8);
        reuse2.task();  //reuse1 and reuse2 are different objects! -> more flexible for reusing
        
        System.out.println("\n\n+++++ Reuse 3 : back to default bounds ");
        reuse1.task();

    }
}
